package service;

import model.UserData;
import dataaccess.UserDAO;

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