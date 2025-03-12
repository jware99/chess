package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.ErrorException;


import java.sql.*;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class MySqlGameDAO implements GameDAO {
    DatabaseManager manager = new DatabaseManager();

    public MySqlGameDAO() throws DataAccessException {
        String[] createStatements = {
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
        manager.configureDatabase(createStatements);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {

                        int resultGameID = resultSet.getInt("gameID");
                        String resultWhiteUsername = resultSet.getString("whiteUsername");
                        String resultBlackUsername = resultSet.getString("blackUsername");
                        String resultGameName = resultSet.getString("gameName");
                        String resultGameTemp = resultSet.getString("game");

                        ChessGame resultGame = new Gson().fromJson(resultGameTemp, ChessGame.class);

                        return new GameData(resultGameID, resultWhiteUsername, resultBlackUsername, resultGameName, resultGame);
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

                if (gameData.gameName() == null) {
                    throw new ErrorException(500, "Failed to retrieve game ID");
                }

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
        var result = new ArrayList<GameData>();
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int gameID = resultSet.getInt("gameID");
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");

                        ChessGame game = new Gson().fromJson(resultSet.getString("game"), ChessGame.class);

                        result.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
        return result;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {

                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, new Gson().toJson(new ChessGame(), ChessGame.class));
                preparedStatement.setInt(5, gameData.gameID());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new ErrorException(500, "Game update failed, no rows affected.");
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
        var statement = "DELETE FROM games";
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErrorException(500, "Database error");
        }
    }

}
