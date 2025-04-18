package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler(UserDAO userDAO, AuthDAO authDao, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDao;
        this.gameDAO = gameDAO;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> makeMove(session, command);
            case LEAVE -> leave(session, command);
            case RESIGN -> resign(session,command);
        }
    }

    private void connect(Session session, UserGameCommand command) throws IOException, DataAccessException {
        if (authDAO.getAuth(command.getAuthToken()) == null) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthenticated user");
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
            return;
        }

        String username = authDAO.getAuth(command.getAuthToken()).username();
        int gameID = command.getGameID();

        var game = gameDAO.getGame(gameID);
        if (game == null) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: game not found");
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
            return;
        }

        String playerColor = getString(username, game);

        connections.add(username, session, gameID);

        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String jsonMessage = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(jsonMessage);

        String notificationMessage = String.format("%s joined the game as %s.", username, playerColor);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification, gameID);
    }

    private void leave(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String username = authDAO.getAuth(command.getAuthToken()).username();
        connections.remove(username);
        GameData gameData = gameDAO.getGame(command.getGameID());

        if (Objects.equals(username, gameData.whiteUsername())) {
            gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (Objects.equals(username, gameData.blackUsername())) {
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        } else {
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        }

        gameDAO.updateGame(gameData);

        String notificationMessage = String.format("%s left the game", username);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification, command.getGameID());
    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String username = authDAO.getAuth(command.getAuthToken()).username();
        int gameID = command.getGameID();
        GameData game = gameDAO.getGame(gameID);

        if (game.game().getResigned()) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: game already ended");
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
            return;
        }

        if (username != null) {
            if (!Objects.equals(username, game.whiteUsername()) && !Objects.equals(username, game.blackUsername())) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: cannot resign as an observer");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }
        }

        String notificationMessage = String.format("%s resigned from the game", username);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);

        game.game().setResigned();
        gameDAO.updateGame(game);


        for (var connection : connections.connections.values()) {
            if (connection.gameID.equals(gameID) && connection.session.isOpen()) {
                connection.send(new Gson().toJson(notification));
            }
        }
    }

    public void makeMove(Session session,UserGameCommand command) throws DataAccessException,IOException,InvalidMoveException {
        try {
            if (command.getAuthToken() == null || authDAO.getAuth(command.getAuthToken()) == null) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: user not authenticated");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }

            String username = authDAO.getAuth(command.getAuthToken()).username();
            int gameID = command.getGameID();
            GameData game = gameDAO.getGame(gameID);

            if (game.game().getResigned()) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: game is already over");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }

            String playerColor;
            playerColor = getString(username, game);
            String oppUsername = "";

            if (playerColor.equals("WHITE")) {
                oppUsername = game.blackUsername();
            } else {
                oppUsername = game.whiteUsername();
            }

            if (playerColor.equals("OBSERVER")) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: cannot move as an observer");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }

            ChessGame.TeamColor teamTurn = game.game().getTeamTurn();
            ChessGame.TeamColor oppColor;

            if (teamTurn == ChessGame.TeamColor.WHITE) {
                oppColor = ChessGame.TeamColor.BLACK;
            } else {
                oppColor = ChessGame.TeamColor.WHITE;
            }

            boolean isWrongTurn = (teamTurn == ChessGame.TeamColor.WHITE && playerColor.equals("BLACK")) ||
                    (teamTurn == ChessGame.TeamColor.BLACK && playerColor.equals("WHITE"));

            if (isWrongTurn) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: not your turn");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }
            if (command.move().getStartPosition() == null) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }

            if (!game.game().validMoves(command.move().getStartPosition()).contains(command.move())) {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
                String jsonMessage = new Gson().toJson(errorMessage);
                session.getRemote().sendString(jsonMessage);
                return;
            }

            game.game().makeMove(command.move());

            gameDAO.updateGame(game);

            String notificationMessage;
            System.out.println("checking for check");
            System.out.println(oppColor);

            ServerMessage notification;

            if (game.game().isInCheck(oppColor)) {
                if (game.game().isInCheckmate(oppColor)) {
                    game.game().setResigned();
                    gameDAO.updateGame(game);
                    notificationMessage = String.format("%s is in checkmate. %s won the game!", oppUsername, username);
                } else if (game.game().isInStalemate(oppColor)) {
                    game.game().setResigned();
                    gameDAO.updateGame(game);
                    notificationMessage = ("stalemate! It's a draw.");
                } else {
                    notificationMessage = String.format("%s is in check!", oppUsername);
                }
                // For important game states, send to everyone including sender
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
                broadcastToAll(notification, gameID);
            }
            notificationMessage = String.format("%s moved from %s to %s", username,
                    convertPositionToChessNotation(command.move().getStartPosition()),
                    convertPositionToChessNotation(command.move().getEndPosition()));
            notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
            connections.broadcast(username, notification, gameID);

            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            broadcastToAll(loadGameMessage, gameID);

        } catch (InvalidMoveException e) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
            return;
        }
    }

    private String convertPositionToChessNotation(chess.ChessPosition position) {
        if (position == null) return "?";

        // Chess columns are numbered 1-8 but represented as A-H
        char column = (char)('A' + position.getColumn() - 1);

        // Chess rows are numbered 1-8, with 1 at the bottom
        int row = position.getRow();

        return String.format("%c%d", column, row);
    }

    // New helper method to broadcast to all connections including sender
    private void broadcastToAll(ServerMessage message, int gameID) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        for (var connection : connections.connections.values()) {
            if (connection.gameID.equals(gameID) && connection.session.isOpen()) {
                connection.send(jsonMessage);
            }
        }
    }

    private String getString(String username, GameData game) throws DataAccessException {
        String playerColor;
        if (username.equals(game.whiteUsername())) {
            playerColor = "WHITE";
        } else if (username.equals(game.blackUsername())) {
            playerColor = "BLACK";
        } else if (game.whiteUsername() == null) {
            playerColor = "WHITE";
            gameDAO.updateGame(new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
        } else if (game.blackUsername() == null) {
            playerColor = "BLACK";
            gameDAO.updateGame(new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
        } else {
            playerColor = "OBSERVER";
        }
        return playerColor;
    }
}