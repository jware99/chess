package handler;

import dataaccess.DataAccessException;
import request.LoginRequest;
import result.ErrorResult;
import result.LoginResult;
import service.ErrorException;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;
import model.UserData;

public class LoginHandler { //convert http to java and back to json

    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object login(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());
        try {
            LoginResult loginResult = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}