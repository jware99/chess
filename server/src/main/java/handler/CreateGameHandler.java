package handler;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.LoginRequest;
import result.CreateGameResult;
import result.ErrorResult;
import result.LoginResult;
import service.ErrorException;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import model.UserData;

public class CreateGameHandler { //convert http to java and back to json

    GameService gameService;

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response res) {
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        String authToken = req.headers("authorization");
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, game.gameName());
        try {
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            res.status(200);
            return new Gson().toJson(createGameResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}