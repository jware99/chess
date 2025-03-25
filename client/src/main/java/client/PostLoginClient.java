package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import model.GameData;
import request.*;
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


    public PostLoginClient(String serverUrl, State state, String authToken) {
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval(State state, String authToken, String input) {
        System.out.println("post eval");
        this.state = state;
        this.authToken = authToken;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(authToken, params);
                case "list" -> list(authToken);
                case "join" -> join(authToken, params);
                case "observe" -> observe(authToken, params);
                case "logout" -> logout(authToken);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String authToken, String... params) throws ResponseException {
        System.out.println("create");
        if (params.length >= 1) {
            System.out.println("0");
            gameID = facade.createGameResult(new CreateGameRequest(authToken, params[0])).gameID();
            System.out.println("1");
            gameName = params[0];
            System.out.println("2");
            return String.format("You created %s.", gameName);
        }
        throw new ResponseException(400, "Error creating game");
    }

    public String list(String authToken) throws ResponseException {
        System.out.println("list");
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

    public String join(String authToken, String... params) throws ResponseException {
        System.out.println("join");
        if (params.length >= 2) {
            ChessGame.TeamColor playerColor = null;
            if (params[1].equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (params[1].equalsIgnoreCase("black")) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                return "invalid player option";
            }
            state = State.INGAME;
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, Integer.parseInt(params[0]));
            facade.joinGameResult(joinGameRequest);
            return "Joined new game!";
        }
        return "Invalid call attempt";
    }

    public String observe(String authToken, String... params) throws ResponseException {
        System.out.println("observe");
        if (params.length >= 1) {
            return "Observing new game!";
        }
        return "Invalid call attempt";
    }

    public String logout(String authToken) throws ResponseException {
        System.out.println("logout");
        state = State.SIGNEDOUT;
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        facade.logoutResult(logoutRequest);
        return ("Successfully logged out");
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

    public String getAuthToken() {
        return authToken;
    }
}
