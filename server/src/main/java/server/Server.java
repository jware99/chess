package server;

import dataaccess.*;
import handler.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server implements Route {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        RegisterHandler registerHandler = new RegisterHandler(new UserService(userDAO, authDAO));
        LoginHandler loginHandler = new LoginHandler(new UserService(userDAO, authDAO));
        LogoutHandler logoutHandler = new LogoutHandler(new UserService(userDAO, authDAO));
        CreateGameHandler createGameHandler = new CreateGameHandler(new GameService(gameDAO, authDAO));
        JoinGameHandler joinGameHandler = new JoinGameHandler(new GameService(gameDAO, authDAO));
        ClearHandler clearHandler = new ClearHandler(userDAO, authDAO, gameDAO);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", registerHandler::register);
        Spark.post("/session", loginHandler::login);
        Spark.delete("/session", logoutHandler::logout);
        Spark.post("/game", createGameHandler::createGame);
        Spark.put("/game", joinGameHandler::joinGame);

        Spark.delete("/db", clearHandler::clear);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
