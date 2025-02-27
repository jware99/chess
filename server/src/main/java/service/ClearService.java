package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.ClearRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.RegisterResult;

import java.util.UUID;

public class ClearService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clear(ClearRequest clearRequest) throws DataAccessException {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            return new ClearResult();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}
