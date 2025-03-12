package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.ErrorException;

public class UserUnitTests {

    private static final UserDAO USER_DAO;

    static {
        try {
            USER_DAO = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final AuthDAO AUTH_DAO;

    static {
        try {
            AUTH_DAO = new MySqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

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
}
