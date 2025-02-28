package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    //users hashmap will hold the username:String and the userData:UserData
    final private HashMap<String, UserData> users = new HashMap<>();

    public MemoryUserDAO() {}

    @Override
    public UserData getUser(String username) {
        return users.getOrDefault(username, null);
    }

    @Override
    public void createUser(UserData userData) {
        users.putIfAbsent(userData.username(), userData);
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(users);
    }

    @Override
    public String toString() {
        return "MemoryUserDAO{" +
                "users=" + users +
                '}';
    }
}
