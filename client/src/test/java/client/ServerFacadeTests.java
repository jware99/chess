package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import server.Server;
import facade.ServerFacade;
import service.ErrorException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost" + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clearResult();

        RegisterRequest registerRequest = new RegisterRequest("myUsername", "myPassword", "myEmail");
        RegisterResult registerResult = facade.registerResult(registerRequest);
        assertNotNull(registerResult);
        authToken = registerResult.authToken();
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
        Assertions.assertThrows(ErrorException.class, () -> facade.registerResult(registerRequest));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        assertNotNull(loginResult);
        assertNotNull(loginResult.authToken());
        assertEquals("username", loginResult.username());
    }

    @Test
    public void loginFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Assertions.assertThrows(ErrorException.class, () -> facade.loginResult(loginRequest));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        LoginResult loginResult = facade.loginResult(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        LogoutResult logoutResult = facade.logoutResult(logoutRequest);

        assertNull(logoutResult);
    }

    @Test
    public void logoutFail() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("myUsername", "myPassword");
        facade.loginResult(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest("wrong token");

        Assertions.assertThrows(ErrorException.class, () -> facade.logoutResult(logoutRequest));

    }

}
