package client;

import client.websocket.NotificationHandler;
import facade.ServerFacade;
import webSocketMessages.Notification;

import java.util.Scanner;

import static client.EscapeSequences.*;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

public class Repl {
    private final PreLoginClient preLoginClient;
    private final ServerFacade facade;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginClient(serverUrl);
        this.facade = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(RED + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}
