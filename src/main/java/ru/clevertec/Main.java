package ru.clevertec;

import ru.clevertec.service.Client;
import ru.clevertec.service.Server;

public class Main {
    private static final int REQUESTS = 100;
    private static final int EXECUTOR_POOL = 10;

    public static void main(String[] args){
        Client client = new Client(REQUESTS,EXECUTOR_POOL);
        Server server = new Server();

        client.processingResponse(server);

        System.out.println(client.getAccumulator());

    }
}
