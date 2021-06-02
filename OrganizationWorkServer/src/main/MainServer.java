package main;

import serviceServer.Server;

/**
 *
 * @author Admin
 */
public class MainServer {
    public static void main(String[] args) {
        Server server = new Server(5000, "dataSet.csv");
        server.runServer();
    }
}
