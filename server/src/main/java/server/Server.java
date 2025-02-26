package server;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.UserService;
import spark.*;
import handler.RegisterHandler;

public class Server implements Route {

    UserDAO userDAO;
    AuthDAO authDAO;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        RegisterHandler registerHandler = new RegisterHandler(new UserService(userDAO, authDAO));
        ClearHandler
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", registerHandler::register);

        Spark.delete("/db", );
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
