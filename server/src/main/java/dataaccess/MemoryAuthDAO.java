package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    //auths hashmap will hold the authToken:String and the authData:AuthData
    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData getAuth(String authToken) {
        return auths.getOrDefault(authToken, null);
    }

    @Override
    public void createAuth(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(auths, that.auths);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(auths);
    }

    @Override
    public String toString() {
        return "MemoryAuthDAO{" +
                "auths=" + auths +
                '}';
    }
}
