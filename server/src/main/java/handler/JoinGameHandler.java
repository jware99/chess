package handler;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import result.CreateGameResult;
import result.ErrorResult;
import result.JoinGameResult;
import result.LoginResult;
import service.ErrorException;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import model.UserData;

public class JoinGameHandler { //convert http to java and back to json

    GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object joinGame(Request req, Response res) {
        JoinGameRequest game = new Gson().fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, game.playerColor(), game.gameID());
        try {
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            res.status(200);
            return new Gson().toJson(joinGameResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}