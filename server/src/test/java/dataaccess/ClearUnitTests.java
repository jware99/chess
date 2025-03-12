package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.LoginResult;
import service.ClearService;
import service.GameService;
import service.UserService;

public class ClearUnitTests {

    private static final UserDAO USER_DAO = DAOFactory.getUserDAO();
    private static final AuthDAO AUTH_DAO = DAOFactory.getAuthDAO();
    private static final GameDAO GAME_DAO = DAOFactory.getGameDAO();

    @BeforeEach
    public void init() throws DataAccessException {
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
        GAME_DAO.setGameID();
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void positiveClear() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email@gmail.com");
        AuthData authData = new AuthData("authToken", "username");
        GameData gameData = new GameData(0, "white", "black", "name", new ChessGame());

        USER_DAO.createUser(userData);
        AUTH_DAO.createAuth(authData);
        int gameID = GAME_DAO.createGame(gameData);

        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();

        Assertions.assertNull(USER_DAO.getUser("username"));
        Assertions.assertNull(AUTH_DAO.getAuth("authToken"));
        Assertions.assertNull(GAME_DAO.getGame(gameID));
    }
}
