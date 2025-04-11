package websocket;

import chess.InvalidMoveException;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(websocket.messages.ServerMessage notification) throws InvalidMoveException;
}
