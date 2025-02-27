package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataaccess.UserDAO;
import request.RegisterRequest;
import result.RegisterResult;
import service.ErrorException;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        try {
            if (username == null || password == null || email == null) {
                throw new ErrorException(400, "Error: bad request");
            }
            if (userDAO.getUser(username) != null) {
                throw new ErrorException(403, "Error: already taken");
            }
            String authToken = createAuthToken();
            userDAO.createUser(new UserData(username, password, email));
            authDAO.createAuth(new AuthData(username, authToken));
            return new RegisterResult(username, authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.toString());
        }
    }
}

//register, login, logout