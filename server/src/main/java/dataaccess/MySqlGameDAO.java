package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO {
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
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
