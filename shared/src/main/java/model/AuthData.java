package model;

import com.google.gson.*;

import javax.lang.model.element.NestingKind;

public record AuthData(String authToken, String username) {

    public AuthData authData(String username) {
        return new AuthData(username, this.authToken);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
