package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
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
        AuthData auth = authDAO.getAuth(authToken);
        try {
            if (gameName == null) {
                throw new ErrorException(400, "Error: bad request");
            }
            if (authToken == null || auth == null) {
                throw new ErrorException(401, "Error: unauthorized");
            }

            GameData game = new GameData(0, null, null, gameName, new ChessGame());
            int gameID = gameDAO.createGame(game);  // The gameID will be generated here

            return new CreateGameResult(gameID);  // Return the generated gameID
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String authToken = joinGameRequest.authToken();
        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        GameData game = gameDAO.getGame(gameID);
        AuthData auth = authDAO.getAuth(authToken);
        try {
            if (game == null || (playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK)) {
                throw new ErrorException(400, "Error: bad request");
            }
            if (authToken == null || auth == null) {
                throw new ErrorException(401, "Error: unauthorized");
            }
            if (game.blackUsername() != null && game.whiteUsername() != null) {
                throw new ErrorException(403, "Error: already taken");
            }
            if (playerColor == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
                throw new ErrorException(403, "Error: already taken");
            }
            if (playerColor == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                throw new ErrorException(403, "Error: already taken");
            }
            if (playerColor == ChessGame.TeamColor.WHITE) {
                gameDAO.updateGame(new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game()));
            } else {
                gameDAO.updateGame(new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game()));
            }
            return new JoinGameResult();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = listGamesRequest.authToken();
        AuthData auth = authDAO.getAuth(authToken);
        try {
            if (authToken == null || auth == null) {
                throw new ErrorException(401, "Error: unauthorized");
            }
            ArrayList<GameData> gameDataArrayList = gameDAO.listGames();
            return new ListGamesResult(gameDataArrayList);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}