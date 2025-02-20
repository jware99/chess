package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {
    UserData getUser(UserData username);

    UserData createUser(UserData userData);
}
