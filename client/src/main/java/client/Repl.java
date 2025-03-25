package client;

import facade.ServerFacade;

import java.util.Scanner;

public class Repl {
    private final PreLoginClient preLoginClient;
    private State state = State.SIGNEDOUT;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginClient(serverUrl);

    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit") || state != State.SIGNEDOUT) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + "LOGGED OUT" + ">>> ");
    }

}
