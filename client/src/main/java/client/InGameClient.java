package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import facade.ServerFacade;
import model.GameData;
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
    private ChessGame currentGame; // Added field to store current game state


    public InGameClient(String serverUrl, State state, String authToken, Integer gameID, NotificationHandler notificationHandler) throws ResponseException {
        this.facade = new ServerFacade(serverUrl);
        this.state = State.INGAME;
        this.authToken = authToken;
        this.notificationHandler = notificationHandler;
        this.gameID = gameID;
        this.ws = new WebSocketFacade(serverUrl, notificationHandler);
        this.currentGame = new ChessGame();
        this.currentGame.getBoard().resetBoard();
    }

    public String eval(String authToken, State state, String input, Integer gameID) throws ResponseException {
        this.authToken = authToken;
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "move" -> move(gameID, params);
            case "highlight" -> highlight(params);
            case "redraw" -> redraw();
            case "resign" -> resign(authToken);
            case "leave" -> leave();
            default -> help();
        };
    }

    public String move(Integer gameID, String... params) throws ResponseException {
        this.gameID = gameID;
        if (params.length < 2) {
            return "Invalid move format. Use 'move A2 A4' format.";
        }

        try {
            String strStartPos = params[0];
            String strEndPos = params[1];
            String[] listStartPos = strStartPos.split("(?<=\\D)(?=\\d)");
            String[] listEndPos = strEndPos.split("(?<=\\D)(?=\\d)");

            char startColChar = Character.toUpperCase(listStartPos[0].charAt(0));
            char endColChar = Character.toUpperCase(listEndPos[0].charAt(0));

            int startColumn = startColChar - 'A' + 1;  // Handles both upper and lowercase
            int endColumn = endColChar - 'A' + 1;

            int startRow = Integer.parseInt(listStartPos[1]);
            int endRow = Integer.parseInt(listEndPos[1]);

            ChessPosition startPosition = new ChessPosition(startRow, startColumn);
            ChessPosition endPosition = new ChessPosition(endRow, endColumn);

            ChessPiece.PieceType promotionPiece = null;
            if (params.length > 2) {
                String promotion = params[2].toUpperCase();
                switch (promotion) {
                    case "QUEEN" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                    case "ROOK" -> promotionPiece = ChessPiece.PieceType.ROOK;
                    case "BISHOP" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                    case "KNIGHT" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                }
            }

            ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
            ws.makeMove(authToken, gameID, move);

            return "Move sent to server...";
        } catch (Exception e) {
            return "Error making move: Use 'move A2 A4' format.";
        }
    }

    public String highlight(String... params) {
        return "";
    }

    public String redraw() {
        if (currentGame != null) {
            ChessBoard.displayBoard(currentGame, teamColor);
            return "";
        } else {
            return "No game to display";
        }
    }

    public String resign(String authToken) throws ResponseException {
        ws.resign(authToken, gameID);
        return ("You have resigned from the game. You may leave.");
    }

    public String leave() throws ResponseException {
        ws.leaveGame(authToken, gameID);
        state = State.SIGNEDIN;
        return ("You have left the game");
    }

    public void observeGame(Integer gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        state = State.INGAME;
        this.teamColor = teamColor;
        this.gameID = gameID;
        ws.joinGame(authToken, gameID);

        // Draw the initial board state when joining
        ChessBoard.displayBoard(currentGame, teamColor);
    }

    public void joinGame(Integer gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        state = State.INGAME;
        this.teamColor = teamColor;
        this.gameID = gameID;
        ws.joinGame(authToken, gameID);

        // Draw the initial board state when joining
        ChessBoard.displayBoard(currentGame, teamColor);
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

    public ChessGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(ChessGame game) {
        this.currentGame = game;
        //redraw(); // Redraw the board whenever the game state is updated
    }
}