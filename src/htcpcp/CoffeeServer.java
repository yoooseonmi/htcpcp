package htcpcp;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class CoffeeServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        CoffeePot pot = new CoffeePot();

        server.createContext("/api/v1/coffees", new CoffeeHandler(pot));

        server.start();
        System.out.println("listening on :8080");
    }
}