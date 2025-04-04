package client;

import facade.ServerFacade;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.Arrays;

public class InGameClient {

    private  String authToken;
    private  State state;
    private ServerFacade facade;
    private WebSocketFacade ws;
    private ServerMessage message;


    public InGameClient(String serverUrl, State state, String authToken, ServerMessage message) {
        ServerFacade facade = new ServerFacade(serverUrl);
        this.state = state;
        this.authToken = authToken;
        this.message = message;
    }

    public String eval(String authToken, State state,  String input) {
        this.authToken = authToken;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "highlight" -> highlight();
            case "redraw" -> redraw();
            case "resign" -> resign();
            case "leave" -> "quit";
            default -> help();
        };
    }

    public static String help() {
        return """
                highlight - possible moves
                redraw - chess board
                resign - current game
                leave - current game
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
