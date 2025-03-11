package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.ErrorException;
import java.nio.charset.StandardCharsets;


import java.sql.*;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class MySqlGameDAO implements GameDAO {

    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {

                        int result_gameID = resultSet.getInt("gameID");
                        String result_whiteUsername = resultSet.getString("whiteUsername");
                        String result_blackUsername = resultSet.getString("blackUsername");
                        String result_gameName = resultSet.getString("gameName");
                        String result_game_temp = resultSet.getString("game");

                        ChessGame result_game = new Gson().fromJson(result_game_temp, ChessGame.class);

                        return new GameData(result_gameID, result_whiteUsername, result_blackUsername, result_gameName, result_game);
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
    public int createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, new Gson().toJson(new ChessGame(), ChessGame.class));

                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Rows inserted: " + affectedRows);

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedID = generatedKeys.getInt(1);
                        System.out.println("Generated game ID: " + generatedID);
                        return generatedID;
                    } else {
                        throw new ErrorException(500, "Failed to retrieve game ID");
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }



    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public int getGameID() throws DataAccessException {
        var statement = "SELECT MAX(gameID) FROM games";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    } else {
                        throw new ErrorException(500, "No game IDs found");
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

    @Override
    public void setGameID() throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(255) DEFAULT NULL,
              `blackUsername` varchar(255) DEFAULT NULL,
              `gameName` varchar(255) NOT NULL,
              `game` blob NOT NULL,
              PRIMARY KEY (`gameID`)
            );
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
