package client;

import exception.ResponseException;
import facade.ServerFacade;
import model.GameData;
import request.CreateGameRequest;
import request.ListGamesRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListGamesResult;

import java.util.ArrayList;
import java.util.Arrays;

public class PostLoginClient {

    private String gameName = null;
    private String authToken;
    private int gameID;
    private final ServerFacade facade;
    private final String serverUrl;
    private State state;


    public PostLoginClient(String serverUrl, State state) {
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state; // Reference to Repl's state
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length >= 1) {
            gameID = facade.createGameResult(new CreateGameRequest(authToken, params[0])).gameID();
            gameName = params[0];
            return String.format("You created %s.", gameName);
        }
        throw new ResponseException(400, "Error creating game");
    }

    public String list() throws ResponseException {
        state = State.SIGNEDIN;
        ListGamesResult listGamesResult = facade.listGamesResult(new ListGamesRequest(authToken));
        ArrayList<GameData> games = listGamesResult.games();

        String header = String.format("%-4s | %-12s | %-10s | %-10s\n",
                "No.", "Game Name", "White", "Black");
        String separator = "-------------------------------------------\n";

        StringBuilder gameString = new StringBuilder();
        gameString.append(header).append(separator);

        for (int i = 0; i < games.size(); i++) {
            GameData game = games.get(i);
            gameString.append(String.format("%-4d | %-12s | %-10s | %-10s\n",
                    i + 1, game.gameName(),
                    game.whiteUsername(), game.blackUsername()));
        }
        return gameString.toString();
    }

    public static String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    public State getState() {
        return state;
    }
}
