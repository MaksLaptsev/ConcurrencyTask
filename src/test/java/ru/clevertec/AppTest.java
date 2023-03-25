package ru.clevertec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.service.Client;
import ru.clevertec.service.Server;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
public class AppTest {
    private static final int REQUESTS = 100;
    private static final int EXECUTOR_POOL = 10;
    private Server server;
    private Client client;


    @BeforeEach
    void setUp(){
        server = new Server();
        client = new Client(REQUESTS,EXECUTOR_POOL);
    }

    @Test
    void checkAppFunctionalCorrectAccumulator(){
        int expected = 5050;
        client.processingResponse(server);
        int actual = client.getAccumulator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verifying the identity of the client(before sending) and server(after client sending) list")
    void checkAppFunctionalCorrectServerListRequestValues(){
        List<Integer> expected = new ArrayList<>(client.getListValues());
        client.processingResponse(server);
        boolean actual = server.getRequestValues().containsAll(expected);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("List size with requests BEFORE sending")
    void checkAppFunctionalCorrectClientRequestsListBeforeSend(){
        int actual = client.getListValues().size();

        assertThat(actual).isEqualTo(REQUESTS);
    }

    @Test
    @DisplayName("List size with requests AFTER sending")
    void checkAppFunctionalCorrectClientRequestsListAfterSend(){
        client.processingResponse(server);
        int actual = client.getListValues().size();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }
}
