package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ErrorException;
import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class MySqlUserDAO implements UserDAO {
    DatabaseManager manager = new DatabaseManager();

    public MySqlUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
        };
        manager.configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM users WHERE username = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {

                        String resultUsername = resultSet.getString("username");
                        String resultPassword = resultSet.getString("password");
                        String resultEmail = resultSet.getString("email");

                        return new UserData(resultUsername, resultPassword, resultEmail);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

        @Override
    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                if (userData.password() == null) {
                    throw new ErrorException(500, "Database error");
                }
                String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM users";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

}
