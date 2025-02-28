package passoff.server;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import service.UserService;

public class UserUnitTests {
    private static final UserDAO userDAO = new MemoryUserDAO();
    private static final AuthDAO authDAO = new MemoryAuthDAO();
    private static final GameDAO gameDAO = new MemoryGameDAO();
    UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Register")
    public void positiveRegister() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        RegisterResult registerResult = userService.register(registerRequest);
        Assertions.assertEquals(userData.username(), registerResult.username(),
                "Response did not have the same username as was registered");
        Assertions.assertNotNull(registerResult.authToken(), "Response did not contain an authentication string");
    }

    //@Test
    //@DisplayName("Negative Register")
    //public void badRegisterRequest() throws DataAccessException {
    //    UserData userData = new UserData("jware99", null, "joshware99@gmail.com");
    //   RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
    //    RegisterResult registerResult = userService.register(registerRequest);
    //
    //}

    @Test
    @DisplayName("Positive Login")
    public void positiveLogin() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);
        Assertions.assertEquals(userData.username(), loginResult.username(),
                "Response did not have the same username as was registered");
        Assertions.assertNotNull(loginResult.authToken(), "Response did not contain an authentication string");
    }

    @Test
    @DisplayName("Positive Logout")
    public void positiveLogout() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");

        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        LoginResult loginResult = userService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        LogoutResult logoutResult = userService.logout(logoutRequest);

        Assertions.assertEquals(new LogoutResult(), logoutResult,
                "Did not successfully logout");
    }
}
