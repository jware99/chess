package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import model.GameData;
import request.*;
import result.ListGamesResult;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PostLoginClient {

    private String authToken;
    private final ServerFacade facade;
    private State state;
    HashMap<Integer, Integer> gameIDs;
    private int gameNumber;
    ArrayList<String> usersInGame;
    private Integer gameID;
    InGameClient inGameClient;
    private final String serverUrl;

    public PostLoginClient(String serverUrl, State state, String authToken, Integer gameID, InGameClient inGameClient) {
        facade = new ServerFacade(serverUrl);
        this.state = state;
        this.authToken = authToken;
        gameNumber = 1;
        this.gameIDs = new HashMap<>();
        this.usersInGame = new ArrayList<>();
        this.gameID = gameID;
        this.serverUrl = serverUrl;
        this.inGameClient = inGameClient;
    }


    public String eval(String username, State state, String authToken, String input) {
        this.state = state;
        this.authToken = authToken;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(authToken, params);
                case "list" -> list(authToken);
                case "join" -> join(username, authToken, params);
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
        if (params.length >= 1) {
            int gameID = facade.createGameResult(new CreateGameRequest(authToken, params[0])).gameID();
            gameIDs.put(gameNumber, gameID);
            gameNumber++;
            String gameName = params[0];
            return String.format("You created %s.", gameName);
        }
        throw new ResponseException(400, "Error creating game");
    }

    public String list(String authToken) throws ResponseException {
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

    public String join(String username, String authToken, String... params) throws ResponseException {
        if (params.length >= 2) {
            ChessGame.TeamColor playerColor = null;
            if (params[1].equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (params[1].equalsIgnoreCase("black")) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                return "invalid player option";
            }

            if (usersInGame.contains(username)) {
                return "You are already in a game";
            }

            // Get the latest game list from the server
            ListGamesResult listGamesResult = facade.listGamesResult(new ListGamesRequest(authToken));
            ArrayList<GameData> games = listGamesResult.games();

            int gameIndex = Integer.parseInt(params[0]) - 1;
            if (gameIndex < 0 || gameIndex >= games.size()) {
                return "Not a valid game";
            }

            GameData selectedGame = games.get(gameIndex);
            int game = selectedGame.gameID();

            // Check if the chosen color spot is already taken
            if (playerColor == ChessGame.TeamColor.WHITE && selectedGame.whiteUsername() != null) {
                return "White player position is already taken";
            }
            if (playerColor == ChessGame.TeamColor.BLACK && selectedGame.blackUsername() != null) {
                return "Black player position is already taken";
            }

            state = State.INGAME;
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, game);
            facade.joinGameResult(joinGameRequest);

            usersInGame.add(username);
            gameID = game;

            // Update the InGameClient with the current game and initialize it
            inGameClient.joinGame(gameID, playerColor);

            return "Joined new game!";
        }
        return "Invalid call attempt";
    }

    public String observe(String authToken, String... params) throws ResponseException {
        if (params.length >= 1) {
            // Get the latest game list from the server
            ListGamesResult listGamesResult = facade.listGamesResult(new ListGamesRequest(authToken));
            ArrayList<GameData> games = listGamesResult.games();

            int gameIndex = Integer.parseInt(params[0]) - 1;
            if (gameIndex < 0 || gameIndex >= games.size()) {
                return "Not a valid game";
            }

            int game = games.get(gameIndex).gameID();
            this.gameID = game;

            // Update the InGameClient with the current game
            this.state = State.INGAME;
            inGameClient.observeGame(gameID, ChessGame.TeamColor.WHITE);

            return "Observing new game!";
        }
        return "Invalid call attempt";
    }

    public String logout(String authToken) throws ResponseException {
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

    public Integer getGameID() {
        return gameID;
    }
}