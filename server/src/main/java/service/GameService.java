package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String gameName = createGameRequest.gameName();
        String authToken = createGameRequest.authToken();
        try {
            if (gameName == null) {
                throw new ErrorException(400, "Error: bad request");
            }
            if (authToken == null) {
                throw new ErrorException(401, "Error: unauthorized");
            }
            GameData game = new GameData(gameDAO.getGameID(), null, null, gameName, new ChessGame());
            gameDAO.createGame(game);
            return new CreateGameResult(gameDAO.getGameID());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}