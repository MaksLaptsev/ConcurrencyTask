package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.entity.RequestEntity;
import ru.clevertec.entity.RequestResponse;
import ru.clevertec.entity.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

class ServerTest {
    private Server server;
    private RequestResponse request;

    @BeforeEach
    void setUp() {
        server = new Server();
        request = RequestEntity.builder().requestValue(1).build();
    }

    @Test
    void checkProcessingRequest() throws InterruptedException {
        RequestResponse actual = server.processingRequest(request);
        RequestResponse expected = ResponseEntity.builder().responseValue(server.getRequestValues().size()).build();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetRequestValues() throws InterruptedException {
        server.processingRequest(request);
        int contains = request.getValue();

        assertThat(server.getRequestValues().contains(contains)).isEqualTo(true);
    }
}
