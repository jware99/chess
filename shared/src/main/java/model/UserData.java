package model;

import com.google.gson.*;

public record UserData(String username, String password, String email) {

    public UserData userData(String username) {
        return new UserData(username, this.password, this.email);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
