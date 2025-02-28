package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.ArrayList;

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

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String authToken = joinGameRequest.authToken();
        String playerColor = joinGameRequest.playerColor().name();
        int gameID = joinGameRequest.gameID();
        GameData game = gameDAO.getGame(gameID);
        try {
            if (game == null) {
                throw new ErrorException(400, "Error: bad request");
            }
            if (authToken == null || authToken.isEmpty()) {
                throw new ErrorException(401, "Error: unauthorized");
            }
            if (game.blackUsername() != null && game.whiteUsername() != null) {
                throw new ErrorException(403, "Error: already taken");
            }
            gameDAO.updateGame(game);
            return new JoinGameResult();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = listGamesRequest.authToken();
        try {
            if (authToken == null || authToken.isEmpty()) {
                throw new ErrorException(401, "Error: unauthorized");
            }
            ArrayList<GameData> gameDataArrayList = gameDAO.listGames();
            return new ListGamesResult(gameDataArrayList);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}