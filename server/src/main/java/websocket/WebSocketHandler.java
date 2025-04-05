package websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
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
//            case RESIGN -> resign(username,command.getGameID());
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

        connections.add(username, session, gameID);

        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String jsonMessage = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(jsonMessage);

        String notificationMessage = String.format("%s joined the game as %s.", username, "color");
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification);
    }

    private void leave(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String username = authDAO.getAuth(command.getAuthToken()).username();
        connections.remove(username);

        String notificationMessage = String.format("%s joined the game as %s.", username, "color");
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage);
        connections.broadcast(username, notification);
    }

}