package client;

import chess.*;
import exception.ResponseException;
import facade.ServerFacade;
import ui.ChessBoard;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Scanner;

public class InGameClient {

    private String authToken;
    private State state;
    private final ServerFacade facade;
    private final WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private Integer gameID;
    private ChessGame.TeamColor teamColor;
    private ChessGame currentGame; // Added field to store current game state


    public InGameClient(String serverUrl,
                        State state,
                        String authToken,
                        Integer gameID,
                        NotificationHandler notificationHandler) throws ResponseException {
        this.facade = new ServerFacade(serverUrl);
        this.state = State.INGAME;
        this.authToken = authToken;
        this.notificationHandler = notificationHandler;
        this.gameID = gameID;
        this.ws = new WebSocketFacade(serverUrl, notificationHandler);
        this.currentGame = new ChessGame();
        this.currentGame.getBoard().resetBoard();
    }

    public String eval(String authToken, State state, String input, Integer gameID) throws ResponseException, InvalidMoveException {
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
        if (teamColor != ChessGame.TeamColor.BLACK && teamColor != ChessGame.TeamColor.WHITE) {
            return "Observers cannot make moves!";
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
        if (params.length < 1) {
            return "Please specify a position (e.g., 'highlight A2')";
        }

        try {
            String strStartPos = params[0];
            String[] listStartPos = strStartPos.split("(?<=\\D)(?=\\d)");

            if (listStartPos.length < 2) {
                return "Invalid position format. Use format like 'A2'";
            }

            char startColChar = Character.toUpperCase(listStartPos[0].charAt(0));
            int startColumn = startColChar - 'A' + 1;
            int startRow = Integer.parseInt(listStartPos[1]);

            ChessPosition startPosition = new ChessPosition(startRow, startColumn);

            if (currentGame.getBoard().getPiece(startPosition) == null) {
                return "No piece at position " + strStartPos;
            }

            ChessBoard.displayBoard(currentGame, teamColor, true, startPosition);
            return "";
        } catch (Exception e) {
            return "Error highlighting position: " + e.getMessage();
        }
    }

    public String redraw() throws InvalidMoveException {
        if (currentGame != null) {
            ChessBoard.displayBoard(currentGame, teamColor, false, null);
            return "";
        } else {
            return "No game to display";
        }
    }

    public String resign(String authToken) throws ResponseException {
        if (teamColor != ChessGame.TeamColor.BLACK && teamColor != ChessGame.TeamColor.WHITE) {
            return "Observers cannot resign!";
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Are you sure you want to resign? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            ws.resign(authToken, gameID);
            return "You have resigned from the game. You may leave.";
        } else {
            return "Resignation cancelled. You are still in the game.";
        }
    }

    public String leave() throws ResponseException {
        ws.leaveGame(authToken, gameID);
        state = State.SIGNEDIN;

        this.teamColor = null;
        return ("You have left the game");
    }

    public void joinGame(Integer gameID, ChessGame.TeamColor teamColor, boolean observer) throws ResponseException, InvalidMoveException {
        state = State.INGAME;
        if (observer) {
            this.teamColor = null;
        } else {
            this.teamColor = teamColor;
        }
        this.gameID = gameID;
        ws.joinGame(authToken, gameID);

        ChessBoard.displayBoard(currentGame, teamColor, false, null);
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
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}