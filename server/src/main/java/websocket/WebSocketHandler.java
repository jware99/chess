package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;


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
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
//            case MAKE_MOVE -> makeMove(session, command.getAuthToken(),command.getGameID(),command.getMove());
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

        String playerColor = null;
        if (username.equals(game.whiteUsername())) {
            playerColor = "WHITE";
        } else if (username.equals(game.blackUsername())) {
            playerColor = "BLACK";
        } else if (game.whiteUsername() == null) {
            // If white position is open, assign as white
            playerColor = "WHITE";
            gameDAO.updateGame(new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game()));
        } else if (game.blackUsername() == null) {
            // If black position is open, assign as black
            playerColor = "BLACK";
            gameDAO.updateGame(new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game()));
        } else {
            // Both positions are filled - user is just an observer
            playerColor = "OBSERVER";
        }

        connections.add(username, session, gameID);

        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String jsonMessage = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(jsonMessage);

        String notificationMessage = String.format("%s joined the game as %s.", username, playerColor);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification);
    }

    private void leave(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String username = authDAO.getAuth(command.getAuthToken()).username();
        connections.remove(username);
        GameData gameData = gameDAO.getGame(command.getGameID());

        if (Objects.equals(username, gameData.whiteUsername())) {
            gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        }

        gameDAO.updateGame(gameData);

        String notificationMessage = String.format("%s left the game", username);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification);
    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String username = authDAO.getAuth(command.getAuthToken()).username();
        int gameID = command.getGameID();
        GameData game = gameDAO.getGame(gameID);

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

        for (var connection : connections.connections.values()) {
            if (connection.gameID.equals(gameID) && connection.session.isOpen()) {
                connection.send(new Gson().toJson(notification));
            }
        }
    }

}