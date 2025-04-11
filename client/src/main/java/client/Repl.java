package client;

import chess.ChessGame;
import exception.ResponseException;
import facade.ServerFacade;
import java.util.Scanner;

import model.GameData;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

public class Repl implements NotificationHandler {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final InGameClient inGameClient;
    private State state;
    private String authToken = null;
    private final ServerFacade facade;
    private String username = null;
    private Integer gameID = 0;

    public Repl(String serverUrl) throws ResponseException {
        this.state = State.SIGNEDOUT; // Initialize state
        inGameClient = new InGameClient(serverUrl, state, authToken, gameID, this);
        preLoginClient = new PreLoginClient(serverUrl, state, authToken, username, inGameClient);
        postLoginClient = new PostLoginClient(serverUrl, state, authToken, gameID, inGameClient);
        this.facade = new ServerFacade(serverUrl);
    }

    public void run() throws ResponseException {
        facade.clearResult();
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        System.out.print(PreLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (true) {
            printPrompt();
            String line = scanner.nextLine();

            if (state == State.SIGNEDOUT) {
                try {
                    result = preLoginClient.eval(line);
                    state = preLoginClient.getState();
                    authToken = preLoginClient.getAuthToken();
                    username = preLoginClient.getUserName();
                    System.out.println(result);
                    if (result.equals("quit")) {
                        break;
                    }
                } catch (Throwable e) {
                    System.out.println(e);
                }
            } else if (state == State.SIGNEDIN) {
                try {
                    result = postLoginClient.eval(username, state, authToken, line);
                    state = postLoginClient.getState();
                    authToken = postLoginClient.getAuthToken();
                    gameID = postLoginClient.getGameID();
                    System.out.println(result);
                    if (result.equals("quit")) {
                        state = State.SIGNEDOUT;
                    }
                } catch (Throwable e) {
                    System.out.println(e);
                }
            } else {
                try {
                    result = inGameClient.eval(authToken, state, line, gameID);
                    state = inGameClient.getState();
                    authToken = inGameClient.getAuthToken();
                    gameID = inGameClient.getGameID();

                    if (state == State.SIGNEDIN && result.equals("You have left the game")) {
                        postLoginClient.removeUserFromGame(username);
                    }

                    System.out.println(result);
                    if (result.equals("quit")) {
                        state = State.SIGNEDIN;
                    }
                } catch (Throwable e) {
                    System.out.println(e);
                }
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + state + ">>> ");
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification.getMessage() != null) {
            System.out.println("Game notification: " + notification.getMessage());
        }
        ServerMessage.ServerMessageType type = notification.getServerMessageType();

        if (type == ServerMessage.ServerMessageType.LOAD_GAME) {
            Object gameObj = notification.getGame();
            if (gameObj instanceof GameData gameData) {

                ChessGame updatedGame = gameData.game();

                if (inGameClient != null) {
                    inGameClient.setCurrentGame(updatedGame);
                    inGameClient.redraw();
                }
            }
        }
    }
}
