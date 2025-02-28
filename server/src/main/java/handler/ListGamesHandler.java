package handler;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import request.LoginRequest;
import result.*;
import service.ErrorException;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import model.UserData;

public class ListGamesHandler { //convert http to java and back to json

    GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            res.status(200);
            return new Gson().toJson(listGamesResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}