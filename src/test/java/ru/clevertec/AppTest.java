package ru.clevertec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.service.Client;
import ru.clevertec.service.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("argsClientsWithAnyRequestsCount")
    void checkAppFunctionalCorrectAccumulator(Client client){
        int expected = getExpectedAccumulator(client.getListValues().size());
        client.processingResponse(server);
        int actual = client.getAccumulator();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("argsClientsWithAnyRequestsCount")
    @DisplayName("Verifying the identity of the client(before sending) and server(after client sending) list")
    void checkAppFunctionalCorrectServerListRequestValues(Client client){
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

    static Stream<Arguments> argsClientsWithAnyRequestsCount(){
        return Stream.of(
                Arguments.of(new Client(100, EXECUTOR_POOL)),
                Arguments.of(new Client(80,EXECUTOR_POOL)),
                Arguments.of(new Client(150,EXECUTOR_POOL)),
                Arguments.of(new Client(50,EXECUTOR_POOL))
        );
    }

    static int getExpectedAccumulator(int i){
        return (int)((1+i)*((double)i/2));
    }
}
