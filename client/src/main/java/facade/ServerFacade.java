package facade;

import com.google.gson.Gson;
import exception.ResponseException;
import request.*;
import result.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerResult(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, null, registerRequest, RegisterResult.class);
    }

    public LoginResult loginResult(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, null, loginRequest, LoginResult.class);
    }

    public LogoutResult logoutResult(LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, logoutRequest.authToken(), null, LogoutResult.class);
    }

    public CreateGameResult createGameResult(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, createGameRequest.authToken(), createGameRequest, CreateGameResult.class);
    }

    public ListGamesResult listGamesResult(ListGamesRequest listGamesRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, listGamesRequest.authToken(), null, ListGamesResult.class);
    }

    public JoinGameResult joinGameResult(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, joinGameRequest.authToken(), joinGameRequest, JoinGameResult.class);
    }

    public void clearResult() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }



    private <T> T makeRequest(String method, String path, String header, Object body, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (header != null) {
                http.setRequestProperty("Authorization", header);
            }

            writeBody(body, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
