package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.ErrorException;

public class UserUnitTests {

    private static final UserDAO USER_DAO = DAOFactory.getUserDAO();
    private static final AuthDAO AUTH_DAO = DAOFactory.getAuthDAO();

    @BeforeEach
    public void init() throws DataAccessException {
        USER_DAO.clear();
        AUTH_DAO.clear();
    }

    @Test
    @DisplayName("Positive createUser")
    public void positiveCreateUser() throws DataAccessException {
        UserData userData = new UserData("jware99", "qwerty", "joshware99@gmail.com");
        USER_DAO.createUser(userData);

        Assertions.assertTrue(BCrypt.checkpw(userData.password(), USER_DAO.getUser(userData.username()).password()));
    }

    @Test
    @DisplayName("Negative createUser")
    public void badCreateUser() throws DataAccessException {
        UserData userData = new UserData("jware99", null, "joshware99@gmail.com");

        Assertions.assertThrows(ErrorException.class, () -> USER_DAO.createUser(userData));
    }

    @Test
    @DisplayName("Positive getUser")
    public void positiveGetUser() throws DataAccessException {
        UserData userData = new UserData("username", "password", "myemail@gmail.com");
        USER_DAO.createUser(userData);

        Assertions.assertTrue(BCrypt.checkpw(userData.password(), USER_DAO.getUser(userData.username()).password()));
    }

    @Test
    @DisplayName("Negative getUser")
    public void badGetUser() throws DataAccessException {
        UserData userData = new UserData("username", "password", "myemail@gmail.com");

        Assertions.assertNull(USER_DAO.getUser(userData.username()));
    }

    @Test
    @DisplayName("Positive createAuth")
    public void positiveCreateAuth() throws DataAccessException {
        AuthData authData = new AuthData("myAuthToken", "username");
        AUTH_DAO.createAuth(authData);

        Assertions.assertEquals(authData.username(), AUTH_DAO.getAuth(authData.authToken()).username());
        Assertions.assertEquals(authData.authToken(), AUTH_DAO.getAuth(authData.authToken()).authToken());
    }

    @Test
    @DisplayName("Negative createAuth")
    public void badCreateAuth() throws DataAccessException {
        AuthData authData = new AuthData("myAuthToken", null);

        Assertions.assertThrows(ErrorException.class, () -> AUTH_DAO.createAuth(authData));
    }

    @Test
    @DisplayName("Positive getAuth")
    public void positiveGetAuth() throws DataAccessException {
        AuthData authData = new AuthData("token", "cougars");
        AUTH_DAO.createAuth(authData);

        Assertions.assertThrows(ErrorException.class, () -> AUTH_DAO.createAuth(authData));
    }

    @Test
    @DisplayName("Negative getAuth")
    public void badGetAuth() throws DataAccessException {
        AuthData authData = new AuthData("token", "cougars");

        Assertions.assertNull(AUTH_DAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Positive removeAuth")
    public void positiveRemoveAuth() throws DataAccessException {
        AuthData authData = new AuthData("myAuthToken", "username");
        AUTH_DAO.createAuth(authData);
        AUTH_DAO.deleteAuth(authData.authToken());
        Assertions.assertNull(AUTH_DAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Negative removeAuth")
    public void badRemoveAuth() throws DataAccessException {
        AuthData authData = new AuthData("myAuthToken", "username");
        AUTH_DAO.createAuth(authData);
        Assertions.assertThrows(ErrorException.class, () -> AUTH_DAO.deleteAuth(null));
    }
}
