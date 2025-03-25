package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import model.GameData;
import request.*;
import result.ListGamesResult;

import java.util.ArrayList;
import java.util.Arrays;

public class InGameClient {

    private String gameName = null;
    private String authToken;
    private int gameID;
    private final ServerFacade facade;
    private final String serverUrl;
    private State state;


    public InGameClient(String serverUrl, State state, String authToken) {
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval(String authToken, State state,  String input) {
        this.authToken = authToken;
        this.state = state;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "quit" -> "quit";
            default -> help();
        };
    }

    public static String help() {
        return """
                quit - playing chess
                help - with possible commands
                """;
    }

    public State getState() {
        return state;
    }

    public String getAuthToken() {
        return authToken;
    }
}
