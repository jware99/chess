package dataaccess;

public class DAOFactory {
    private static final UserDAO USER_DAO;
    private static final AuthDAO AUTH_DAO;
    private static final GameDAO GAME_DAO;

    static {
        try {
            USER_DAO = new MySqlUserDAO();
            AUTH_DAO = new MySqlAuthDAO();
            GAME_DAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize DAOFactory", e);
        }
    }

    public static UserDAO getUserDao() {
        return USER_DAO;
    }

    public static AuthDAO getAuthDao() {
        return AUTH_DAO;
    }

    public static GameDAO getGameDao() {
        return GAME_DAO;
    }
}
