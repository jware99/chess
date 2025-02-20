package service;

import dataaccess.DataAccessException;
import model.UserData;
import dataaccess.UserDAO;
import org.eclipse.jetty.server.Authentication;
import passoff.exception.ResponseParseException;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO dataAccess) {
        this.userDAO = dataAccess;
    }

    public UserData getUser(UserData username) {
        return userDAO.getUser(username);
    }

    public UserData createUser(UserData username) {
        return userDAO.createUser(username);
    }

}

//register, login, logout