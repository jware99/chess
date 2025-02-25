package model;

import com.google.gson.*;


public record AuthData(String authToken, String username) {

    public AuthData authData(String username) {
        return new AuthData(username, this.authToken);
    }

    @Override
    public String authToken() {
        return authToken;
    }

    @Override
    public String username() {
        return username;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
