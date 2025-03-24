package server;

import dataaccess.*;
import handler.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    UserDAO userDAO;

    {
        try {
            userDAO = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    AuthDAO authDAO;

    {
        try {
            authDAO = new MySqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    GameDAO gameDAO;

    {
        try {
            gameDAO = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() {
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        RegisterHandler registerHandler = new RegisterHandler(new UserService(userDAO, authDAO));
        LoginHandler loginHandler = new LoginHandler(new UserService(userDAO, authDAO));
        LogoutHandler logoutHandler = new LogoutHandler(new UserService(userDAO, authDAO));
        CreateGameHandler createGameHandler = new CreateGameHandler(new GameService(gameDAO, authDAO));
        JoinGameHandler joinGameHandler = new JoinGameHandler(new GameService(gameDAO, authDAO));
        ListGamesHandler listGamesHandler = new ListGamesHandler(new GameService(gameDAO, authDAO));
        ClearHandler clearHandler = new ClearHandler(userDAO, authDAO, gameDAO);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", registerHandler::register);
        Spark.post("/session", loginHandler::login);
        Spark.delete("/session", logoutHandler::logout);
        Spark.post("/game", createGameHandler::createGame);
        Spark.put("/game", joinGameHandler::joinGame);
        Spark.get("/game", listGamesHandler::listGames);
        Spark.delete("/db", clearHandler::clear);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
