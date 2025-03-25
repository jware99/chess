package client;

import exception.ResponseException;
import facade.ServerFacade;
import request.LoginRequest;
import request.RegisterRequest;

import java.util.Arrays;

public class PreLoginClient {
    private String visitorName = null;
    private String authToken;
    private final ServerFacade facade;
    private final String serverUrl;
    private State state;

    public PreLoginClient(String serverUrl, State state, String authToken) {
        this.facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.state = state;
        this.authToken = authToken;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
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
            authToken = facade.registerResult(new RegisterRequest(params[0], params[1], params[2])).authToken();
            visitorName = params[0];
            state = State.SIGNEDIN;
            return String.format("You registered as %s.", visitorName);
        }
        return ("Error registering");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            authToken = facade.loginResult(new LoginRequest(params[0], params[1])).authToken();
            visitorName = params[0];
            state = State.SIGNEDIN; // Update state
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Error signing in");
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
}