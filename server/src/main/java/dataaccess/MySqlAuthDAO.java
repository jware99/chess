package dataaccess;

import model.AuthData;
import model.UserData;
import service.ErrorException;

import java.sql.*;

import static dataaccess.DatabaseManager.getConnection;

public class MySqlAuthDAO implements AuthDAO {

    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM auths WHERE authToken = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {

                        String result_authToken = resultSet.getString("authToken");
                        String result_username = resultSet.getString("username");

                        return new AuthData(result_authToken, result_username);
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
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "DELETE FROM auths";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }
}
