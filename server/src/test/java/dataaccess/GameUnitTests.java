package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.ErrorException;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class GameUnitTests {

    private static final UserDAO USER_DAO;

    static {
        try {
            USER_DAO = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final AuthDAO AUTH_DAO;

    static {
        try {
            AUTH_DAO = new MySqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final GameDAO GAME_DAO;

    static {
        try {
            GAME_DAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void init() throws DataAccessException {
        USER_DAO.clear();
        AUTH_DAO.clear();
        GAME_DAO.clear();
    }

    @Test
    @DisplayName("Positive createGame")
    public void positiveCreateGame() throws DataAccessException {
        GameData gameData = new GameData(0, "white", "black", "new_game", new ChessGame());
        int gameID = GAME_DAO.createGame(gameData);

        Assertions.assertEquals(gameID, GAME_DAO.getGame(gameID).gameID());
        Assertions.assertEquals(gameData.whiteUsername(), GAME_DAO.getGame(gameID).whiteUsername());
        Assertions.assertEquals(gameData.blackUsername(), GAME_DAO.getGame(gameID).blackUsername());
        Assertions.assertEquals(gameData.gameName(), GAME_DAO.getGame(gameID).gameName());
        Assertions.assertEquals(gameData.game(), GAME_DAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Negative createGame")
    public void badCreateGame() throws DataAccessException {
        GameData gameData = new GameData(0, "white", "black", null, new ChessGame());

        Assertions.assertThrows(ErrorException.class, () -> GAME_DAO.createGame(gameData));
    }

    @Test
    @DisplayName("Positive getGame")
    public void positiveGetGame() throws DataAccessException {
        GameData gameData = new GameData(0, null, "josh", "masters only", new ChessGame());
        int gameID = GAME_DAO.createGame(gameData);

        Assertions.assertEquals(gameID, GAME_DAO.getGame(gameID).gameID());
        Assertions.assertEquals(gameData.whiteUsername(), GAME_DAO.getGame(gameID).whiteUsername());
        Assertions.assertEquals(gameData.blackUsername(), GAME_DAO.getGame(gameID).blackUsername());
        Assertions.assertEquals(gameData.gameName(), GAME_DAO.getGame(gameID).gameName());
        Assertions.assertEquals(gameData.game(), GAME_DAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Negative getGame")
    public void badGetGame() throws DataAccessException {
        GameData gameData = new GameData(0, null, "josh", "masters only", new ChessGame());
        GAME_DAO.createGame(gameData);

        Assertions.assertNull(GAME_DAO.getGame(65));
    }

    @Test
    @DisplayName("Positive listGames")
    public void positiveListGames() throws DataAccessException {
        ArrayList<GameData> gameList = new ArrayList<>();
        GameData gameOne = new GameData(0, null, null, "Josh's Game", new ChessGame());
        GameData gameTwo = new GameData(1, null, null, "New Game", new ChessGame());
        GameData gameThree = new GameData(2, null, null, "Chess Masters", new ChessGame());

        int firstGameID = GAME_DAO.createGame(gameOne);
        int secondGameID = GAME_DAO.createGame(gameTwo);
        int thirdGameID = GAME_DAO.createGame(gameThree);

        gameList.add(new GameData(firstGameID, null, null, "Josh's Game", new ChessGame()));
        gameList.add(new GameData(secondGameID , null, null, "New Game", new ChessGame()));
        gameList.add(new GameData(thirdGameID, null, null, "Chess Masters", new ChessGame()));

        Assertions.assertEquals(gameList, GAME_DAO.listGames(),
                "Unable to join a game");
    }

    @Test
    @DisplayName("Negative listGames")
    public void negativeListGames() throws DataAccessException {

        Assertions.assertEquals(new ArrayList<>(), GAME_DAO.listGames());
    }

    @Test
    @DisplayName("Positive updateGame")
    public void positiveUpdateGame() throws DataAccessException {
        GameData gameData = new GameData(0, null, "josh", "masters only", new ChessGame());
        int gameID = GAME_DAO.createGame(gameData);

        GameData newGameData = new GameData(gameID, "alex", "josh", "masters only", new ChessGame());

        GAME_DAO.updateGame(newGameData);

        Assertions.assertNotEquals(gameData.whiteUsername(), GAME_DAO.getGame(gameID).whiteUsername());
        Assertions.assertEquals(newGameData.whiteUsername(), GAME_DAO.getGame(gameID).whiteUsername());

    }

    @Test
    @DisplayName("Negative updateGame")
    public void negativeUpdateGame() throws DataAccessException {
        GameData gameData = new GameData(0, null, "josh", "masters only", new ChessGame());
        GAME_DAO.createGame(gameData);

        GameData newGameData = new GameData(11, "alex", "josh", "masters only", new ChessGame());

        Assertions.assertThrows(ErrorException.class, () -> GAME_DAO.updateGame(newGameData));

    }
}
