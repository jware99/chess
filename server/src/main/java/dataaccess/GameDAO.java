package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;

    int createGame(GameData gameData) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    int getGameID() throws DataAccessException;

    void setGameID() throws DataAccessException;

    void clear() throws DataAccessException;

}
