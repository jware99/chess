package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 0;

    @Override
    public GameData getGame(int gameID) {
        return games.getOrDefault(gameID, null);
    }

    @Override
    public int createGame(GameData gameData) {
        int assignedGameID = gameID;  // Assign current gameID
        GameData newGameData = new GameData(assignedGameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        games.putIfAbsent(gameID, newGameData);
        gameID++;
        return gameData.gameID();
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public void setGameID() {
        gameID = 0;
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(games);
    }

    @Override
    public String toString() {
        return "MemoryGameDAO{" +
                "games=" + games +
                '}';
    }
}
