package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.UserData;
import request.RegisterRequest;
import result.ClearResult;
import result.RegisterResult;
import service.ErrorException;
import service.UserService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public String clear(Request req, Response res) {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            return new Gson().toJson(new ClearResult());
        }
        catch (ErrorException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //loginHandler, registerHandler, logoutHandler, joingameHandler, creategameHandler, listGamesHandler, clearHandler
}
