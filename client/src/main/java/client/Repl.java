package client;

import exception.ResponseException;
import facade.ServerFacade;
import java.util.Scanner;
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
        preLoginClient = new PreLoginClient(serverUrl, state, authToken, username);
        postLoginClient = new PostLoginClient(serverUrl, state, authToken, gameID);
        inGameClient = new InGameClient(serverUrl, state, authToken, gameID, this);
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
                    System.out.println(e.toString());
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
                    System.out.println(e.toString());
                }
            } else {
                try {
                    result = inGameClient.eval(authToken, state, line);
                    state = inGameClient.getState();
                    authToken = inGameClient.getAuthToken();
                    System.out.println(result);
                    if (result.equals("quit")) {
                        state = State.SIGNEDIN;
                    }
                } catch (Throwable e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + state + ">>> ");
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(notification.getServerMessageType());
        printPrompt();
    }
}
