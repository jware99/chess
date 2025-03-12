package dataaccess;

public class DAOFactory {
    private static final UserDAO userDAO;
    private static final AuthDAO authDAO;
    private static final GameDAO gameDAO;

    static {
        try {
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();
            gameDAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize DAOFactory", e);
        }
    }

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public static AuthDAO getAuthDAO() {
        return authDAO;
    }

    public static GameDAO getGameDAO() {
        return gameDAO;
    }
}
