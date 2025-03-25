package client;

import exception.ResponseException;
import facade.ServerFacade;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.ArrayList;
import java.util.Arrays;

public class PreLoginClient {
    private String authToken;
    private final ServerFacade facade;
    private State state;
    private String username;
    ArrayList<String> usernames;

    public PreLoginClient(String serverUrl, State state, String authToken, String username) {
        this.facade = new ServerFacade(serverUrl);
        this.state = state;
        this.authToken = authToken;
        this.username = username;
        this.usernames = new ArrayList<>();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            state = State.SIGNEDOUT;
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            if (!usernames.contains(params[0])) {
                    RegisterResult registerResult = facade.registerResult(new RegisterRequest(params[0], params[1], params[2]));
                    usernames.add(params[0]);
                    authToken = registerResult.authToken();
                    username = params[0];
                    state = State.SIGNEDIN;
                    return String.format("You registered as %s.", username);
                }
                return ("Already registered");
        } else {
            state = State.SIGNEDOUT;
            return ("Error registering");
        }

    }

    public String login(String... params) {
        if (params.length < 2) {
            state = State.SIGNEDOUT;
            return "Error signing in: Missing username or password.";
        }

        try {
            authToken = facade.loginResult(new LoginRequest(params[0], params[1])).authToken();
            username = params[0];
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", username);
        } catch (ResponseException e) {
            return "Check to see you logged in correctly";
        }
    }

    public static String help() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
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

    public String getUserName() {
        return username;
    }
}