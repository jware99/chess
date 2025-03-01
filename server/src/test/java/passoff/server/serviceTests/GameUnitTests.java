package passoff.server.serviceTests;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;
import service.ErrorException;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

public class GameUnitTests {
    private static final UserDAO USER_DAO = new MemoryUserDAO();
    private static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    private static final GameDAO GAME_DAO = new MemoryGameDAO();
    UserService userService;
    GameService gameService;

    @BeforeEach
    public void init() throws DataAccessException {
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
        GAME_DAO.setGameID();
        userService = new UserService(USER_DAO, AUTH_DAO);
        gameService = new GameService(GAME_DAO, AUTH_DAO);
    }

    @Test
    @DisplayName("Positive Create Game")
    public void positiveCreateGame() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "Josh's Game");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        Assertions.assertEquals(new CreateGameResult(1), createGameResult,
                "Unable to create a new game");
    }

    @Test
    @DisplayName("Negative Create Game")
    public void badCreateGameRequest() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("notanauthtoken", "myGame");

        Assertions.assertThrows(ErrorException.class, () -> gameService.createGame(createGameRequest));
    }

    @Test
    @DisplayName("Positive Join Game")
    public void positiveJoinGame() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "Josh's Game");
        gameService.createGame(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.BLACK, 1);
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);

        Assertions.assertEquals("jware99", GAME_DAO.getGame(1).blackUsername());
    }

    @Test
    @DisplayName("Negative Join Game")
    public void badJoinGameRequest() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "myGame");
        gameService.createGame(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.BLACK, 10);

        Assertions.assertThrows(ErrorException.class, () -> gameService.joinGame(joinGameRequest));
    }

    @Test
    @DisplayName("Positive List Games")
    public void positiveListGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        gameList.add(new GameData(1, null, null, "Josh's Game", new ChessGame()));
        gameList.add(new GameData(2, null, null, "New Game", new ChessGame()));
        gameList.add(new GameData(3, null, null, "Chess Masters", new ChessGame()));

        Assertions.assertEquals(gameList, listGamesResult().games(),
                "Unable to join a game");
    }

    public ListGamesResult listGamesResult() throws DataAccessException {
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
        return gameService.listGames(listGamesRequest);
    }

    @Test
    @DisplayName("Negative List Games")
    public void badListGamesRequest() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "myGame");
        gameService.createGame(createGameRequest);

        ListGamesRequest listGamesRequest = new ListGamesRequest("authToken");

        Assertions.assertThrows(ErrorException.class, () -> gameService.listGames(listGamesRequest));
    }
}
