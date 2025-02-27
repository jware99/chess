package handler;

import dataaccess.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import service.ErrorException;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import model.UserData;

public class RegisterHandler { //convert http to java and back to json

    UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) {
        System.out.println("register handler");
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
            RegisterResult registerResult = userService.register(registerRequest);
            res.status(200);
            return new Gson().toJson(registerResult);
        }
        catch (ErrorException | DataAccessException e) {
            res.status(400);
            return new Gson().toJson("Error bad request");

        }
    }
    //loginHandler, registerHandler, logoutHandler, joingameHandler, creategameHandler, listGamesHandler, clearHandler
}
