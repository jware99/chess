package handler;

import dataaccess.DataAccessException;
import request.RegisterRequest;
import result.ErrorResult;
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
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            res.status(200);
            return new Gson().toJson(registerResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //loginHandler, registerHandler, logoutHandler, joingameHandler, creategameHandler, listGamesHandler, clearHandler
}
