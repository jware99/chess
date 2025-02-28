package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.*;
import result.ClearResult;
import result.LoginResult;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

public class ClearUnitTest {
    private static final UserDAO USER_DAO = new MemoryUserDAO();
    private static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    private static final GameDAO GAME_DAO = new MemoryGameDAO();
    UserService userService;
    GameService gameService;
    ClearService clearService;

    @BeforeEach
    public void init() throws DataAccessException {
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
        GAME_DAO.setGameID();
        userService = new UserService(USER_DAO, AUTH_DAO);
        gameService = new GameService(GAME_DAO, AUTH_DAO);
        clearService = new ClearService(USER_DAO, AUTH_DAO, GAME_DAO);
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void positiveClear() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        gameList.add(new GameData(1, null, null, "Josh's Game", new ChessGame()));
        gameList.add(new GameData(2, null, null, "New Game", new ChessGame()));
        gameList.add(new GameData(3, null, null, "Chess Masters", new ChessGame()));


        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "Josh's Game");
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(loginResult.authToken(), "New Game");
        gameService.createGame(createGameRequest1);

        CreateGameRequest createGameRequest2 = new CreateGameRequest(loginResult.authToken(), "Chess Masters");
        gameService.createGame(createGameRequest2);

        ListGamesRequest listGamesRequest = new ListGamesRequest(loginResult.authToken());
        gameService.listGames(listGamesRequest);

        ClearRequest clearRequest = new ClearRequest();
        ClearResult clearResult = clearService.clear(clearRequest);

        Assertions.assertEquals(new ClearResult(), clearResult,
                "couldn't clear");
    }
}
