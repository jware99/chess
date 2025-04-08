package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, websocket.Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, Integer gameID) {
        var connection = new websocket.Connection(visitorName, session, gameID);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, ServerMessage message, Integer gameID) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        var removeList = new ArrayList<websocket.Connection>();

        for (var c : connections.values()) {
            if (c.session.isOpen()) {

                if (c.gameID.equals(gameID) && !c.visitorName.equals(excludeVisitorName)) {
                    c.send(jsonMessage);
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
