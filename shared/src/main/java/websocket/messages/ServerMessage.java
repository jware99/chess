package websocket.messages;

import chess.ChessGame;
import model.GameData;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;   // Used specifically for error messages
    GameData game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage(ServerMessageType type, String message) {
        this.serverMessageType = type;
        if (type == ServerMessageType.ERROR) {
            this.errorMessage = message;
        } else {
            this.message = message;
        }
    }

    public ServerMessage(ServerMessageType type, GameData game) {
        this.serverMessageType = type;
        this.game = game;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Object getGame() {
        return this.game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
