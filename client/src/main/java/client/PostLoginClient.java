package client;

import exception.ResponseException;
import facade.ServerFacade;
import request.LoginRequest;
import request.RegisterRequest;

import java.util.Arrays;

public class PostLoginClient {

    private String visitorName = null;
    private String authToken;
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
                case "list" -> login(params);
                case "join" -> login(params);
                case "observe" -> login(params);
                case "logout" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            state = State.SIGNEDIN;
            authToken = facade.registerResult(new RegisterRequest(params[0], params[1], params[2])).authToken();
            visitorName = params[0];
            return String.format("You registered as %s.", visitorName);
        }
        throw new ResponseException(400, "Error registering");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            authToken = facade.loginResult(new LoginRequest(params[0], params[1])).authToken();
            visitorName = params[0];
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Error signing in");
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
