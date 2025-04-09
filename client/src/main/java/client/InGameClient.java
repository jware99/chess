package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import facade.ServerFacade;
import request.CreateGameRequest;
import ui.ChessBoard;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.Arrays;

public class InGameClient {

    private String authToken;
    private State state;
    private ServerFacade facade;
    private WebSocketFacade ws;
    private NotificationHandler notificationHandler;
    private Integer gameID;
    private ChessGame.TeamColor teamColor;


    public InGameClient(String serverUrl, State state, String authToken, Integer gameID, NotificationHandler notificationHandler) throws ResponseException {
        ServerFacade facade = new ServerFacade(serverUrl);
        this.state = State.INGAME;
        this.authToken = authToken;
        this.notificationHandler = notificationHandler;
        this.gameID = gameID;
        this.ws = new WebSocketFacade(serverUrl, notificationHandler);
    }

    public String eval(String authToken, State state,  String input) throws ResponseException {
        this.authToken = authToken;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "move" -> move(params);
            case "highlight" -> highlight(params);
            case "redraw" -> redraw();
            case "resign" -> resign(authToken);
            case "leave" -> "quit";
            default -> help();
        };
    }

    public String move(String... params) throws ResponseException {
        String strStartPos = params[0];
        String strEndPos = params[1];
        String[] listStartPos = strStartPos.split("(?<=\\D)(?=\\d)");
        String[] listEndPos = strEndPos.split("(?<=\\D)(?=\\d)");

        int startColumn = (int) listStartPos[0].charAt(0) - 96;
        int endColumn = (int) listEndPos[0].charAt(0) - 96;

        int startRow = Integer.parseInt(listStartPos[1]);
        int endRow = Integer.parseInt(listEndPos[1]);

        ChessPosition startPosition = new ChessPosition(startRow, startColumn);
        ChessPosition endPosition = new ChessPosition(endRow, endColumn);
        ChessMove move = new ChessMove(startPosition, endPosition, null);
        ws.makeMove(authToken, gameID, move);
        return "moved piece";
    }

    public String highlight(String... params) {
        return "";
    }

    public String redraw() {
        ChessBoard.createBoard(ChessGame.TeamColor.WHITE);
        return "";
    }

    public String resign(String authToken) throws ResponseException {
        ws.resign(authToken, gameID);
        return ("You have resigned from the game. You may leave.");
    }

    public void joinGame(Integer gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        state = State.INGAME;
        this.teamColor = teamColor;
        this.gameID = gameID;
        ws.joinGame(authToken, gameID);
    }

    public static String help() {
        return """
                move <start> <end> (ex: A1 A5) - chess piece
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

    public Integer getGameID() {
        return gameID;
    }
}
