package client;

import facade.ServerFacade;
import java.util.Scanner;

public class Repl {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private State state; // Shared state reference

    public Repl(String serverUrl) {
        this.state = State.SIGNEDOUT; // Initialize state
        preLoginClient = new PreLoginClient(serverUrl, state);
        postLoginClient = new PostLoginClient(serverUrl, state);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        System.out.print(PreLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            if (state == State.SIGNEDOUT) {
                try {
                    result = preLoginClient.eval(line);
                    state = preLoginClient.getState(); // Sync state
                    System.out.println(result);
                } catch (Throwable e) {
                    System.out.println(e.toString());
                }
            } else if (state == State.SIGNEDIN) {
                try {
                    result = postLoginClient.eval(line);
                    state = postLoginClient.getState(); // Sync state
                    System.out.println(result);
                } catch (Throwable e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + state + ">>> ");
    }
}
