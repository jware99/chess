package model;

import com.google.gson.*;

public record UserData(String username, String password, String email) {

    public UserData userData(String username) {
        return new UserData(username, this.password, this.email);
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String email() {
        return email;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
