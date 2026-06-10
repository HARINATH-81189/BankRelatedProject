package demo;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.docs.DocService;

public class Main {

    static Server newServer(int port) {

        return Server.builder()
                .http(port)
                .annotatedService(new BankService())
              .serviceUnder("/docs", DocService.builder().build())
                .build();
    }
    public static void main(String[] args) {

        Server server=newServer(8080);

        server.start().join();

        System.out.println("Server started at http://localhost:8080");
    }
}