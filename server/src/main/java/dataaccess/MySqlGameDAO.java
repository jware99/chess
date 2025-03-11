package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import service.ErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class MySqlGameDAO implements GameDAO {
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
        return 0;
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
        return 0;
    }

    @Override
    public void setGameID() throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
