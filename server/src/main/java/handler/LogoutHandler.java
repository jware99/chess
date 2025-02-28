package handler;

import dataaccess.DataAccessException;
import model.AuthData;
import request.LogoutRequest;
import result.ErrorResult;
import result.LogoutResult;
import service.ErrorException;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.*;

public class LogoutHandler { //convert http to java and back to json

    UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object logout(Request req, Response res) {
        try {
            LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
            LogoutResult logoutResult = userService.logout(logoutRequest);
            res.status(200);
            return new Gson().toJson(logoutResult);
        }
        catch (ErrorException e) {
            res.status(e.getStatus());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}