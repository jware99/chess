package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(AuthData authToken);

    AuthData createAuth(AuthData authData);

    void deleteAuth(AuthData authToken);
}
