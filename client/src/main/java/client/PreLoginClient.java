package client;

import facade.ServerFacade;

public class PreLoginClient {

    private final ServerFacade facade;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
}
