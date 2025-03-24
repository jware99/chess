package client;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;
import facade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clearResult();

        RegisterRequest registerRequest = new RegisterRequest("myUsername", "myPassword", "myEmail");
        RegisterResult registerResult = facade.registerResult(registerRequest);
        assertNotNull(registerResult);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = facade.registerResult(registerRequest);

        assertNotNull(registerResult);
        assertNotNull(registerResult.authToken());
        assertEquals("username", registerResult.username());
    }

    @Test
    public void registerFail() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(null, "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> facade.registerResult(registerRequest));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        assertNotNull(loginResult);
        assertNotNull(loginResult.authToken());
        assertEquals("myUsername", loginResult.username());
    }

    @Test
    public void loginFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Assertions.assertThrows(ResponseException.class, () -> facade.loginResult(loginRequest));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        LogoutResult logoutResult = facade.logoutResult(logoutRequest);

        assertNotNull(logoutResult);
    }

    @Test
    public void logoutFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        facade.loginResult(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest("wrong token");

        Assertions.assertThrows(ResponseException.class, () -> facade.logoutResult(logoutRequest));

    }

    @Test
    public void createGameSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "myGame");
        CreateGameResult createGameResult = facade.createGameResult(createGameRequest);

        assertEquals(new CreateGameResult(createGameResult.gameID()), createGameResult);
    }

    @Test
    public void createGameFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");

        CreateGameRequest createGameRequest = new CreateGameRequest("not an auth token", "myGame");

        Assertions.assertThrows(ResponseException.class, () -> facade.createGameResult(createGameRequest));

    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "newGame");
        CreateGameResult createGameResult = facade.createGameResult(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest(loginResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());
        JoinGameResult joinGameResult = facade.joinGameResult(joinGameRequest);

        assertNotNull(joinGameResult);
    }

    @Test
    public void joinGameFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "newGame");
        CreateGameResult createGameResult = facade.createGameResult(createGameRequest);

        JoinGameRequest joinGameRequest = new JoinGameRequest("not an auth token", ChessGame.TeamColor.WHITE, createGameResult.gameID());

        Assertions.assertThrows(ResponseException.class, () -> facade.joinGameResult(joinGameRequest));
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "newGame");
        facade.createGameResult(createGameRequest);

        ListGamesRequest listGamesRequest = new ListGamesRequest(loginResult.authToken());
        ListGamesResult listGamesResult = facade.listGamesResult(listGamesRequest);

        assertFalse(listGamesResult.games().isEmpty());
    }

    @Test
    public void listGamesFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "newGame");
        facade.createGameResult(createGameRequest);

        ListGamesRequest listGamesRequest = new ListGamesRequest("not my auth");

        Assertions.assertThrows(ResponseException.class, () -> facade.listGamesResult(listGamesRequest));
    }

    @Test
    public void clearSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "newGame");
        facade.createGameResult(createGameRequest);

        facade.clearResult();

        Assertions.assertThrows(ResponseException.class, () -> facade.createGameResult(createGameRequest));
        Assertions.assertThrows(ResponseException.class, () -> facade.loginResult(loginRequest));
    }

}
