package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.entity.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientTest {

    private static final int REQUESTS = 10;
    private static final int EXECUTOR_POOL = 1;
    @Mock
    private Server server;
    private Client client;

    @BeforeEach
    void setUp() throws InterruptedException {
        server = Mockito.mock(Server.class);
        client = new Client(REQUESTS,EXECUTOR_POOL);
        doReturn(ResponseEntity.builder().responseValue(1).build()).when(server).processingRequest(any());
    }

    @Test
    void checkProcessingResponse() throws InterruptedException {
        client.processingResponse(server);
        verify(server, times(REQUESTS)).processingRequest(any());
    }

    @Test
    void checkGetAccumulator() {
        client.processingResponse(server);
        int expected = 10;
        int actual = client.getAccumulator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetListValues(){
        client.processingResponse(server);
        int expected = 0;
        int actual = client.getListValues().size();

        assertThat(actual).isEqualTo(expected);
    }
}
