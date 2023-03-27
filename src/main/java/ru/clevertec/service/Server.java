package ru.clevertec.service;

import ru.clevertec.entity.RequestResponse;
import ru.clevertec.entity.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final Lock lock = new ReentrantLock();
    private List<Integer> requestValues;

    public Server() {
        requestValues = new ArrayList<>();
    }

    public RequestResponse processingRequest(RequestResponse request) throws InterruptedException {
        Thread.sleep(getRandomSleepTime());

        lock.lock();
        try {
            requestValues.add(request.getValue());
            return ResponseEntity.builder().responseValue(requestValues.size()).build();
        }finally {
            lock.unlock();
        }
    }

    private Long getRandomSleepTime(){
        Random random = new Random();
        return random.nextLong(100,1000);
    }

    public List<Integer> getRequestValues() {
        return requestValues;
    }
}
