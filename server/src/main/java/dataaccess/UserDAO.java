package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {
    UserData getUser(String username);

    void createUser(UserData userData);

    void clear();
}
