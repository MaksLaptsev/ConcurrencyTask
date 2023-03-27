package ru.clevertec.service;

import ru.clevertec.entity.RequestEntity;
import ru.clevertec.entity.RequestResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private final Lock lock = new ReentrantLock();
    private final ExecutorService executorService;
    private List<Integer> listValues;
    private List<Future<RequestResponse>> futureList;
    private int accumulator;
    private int requestCounts;

    public Client(int requestCounts) {
        executorService = Executors.newFixedThreadPool(5);
        listValues = initialList(requestCounts);
        futureList = new ArrayList<>();
    }

    public Client(int requestCounts, int threadPool) {
        executorService = Executors.newFixedThreadPool(threadPool);
        listValues = initialList(requestCounts);
        futureList = new ArrayList<>();
        this.requestCounts = requestCounts;
    }

    public void processingResponse(Server server){
        while (requestCounts > 0){
            requestCounts -= 1;
            futureList.add(executorService.submit(
                    () -> server.processingRequest(getRandomEntityForRequest())
            ));
        }
        executorService.shutdown();
        countAccumulator();
    }

    private RequestResponse getRandomEntityForRequest(){
        RequestResponse request;
        lock.lock();
        try {
            if(!listValues.isEmpty()){
                request = RequestEntity.builder()
                        .requestValue(listValues.remove(getValueWithRandomIndex()))
                        .build();
                return request;
            }else return RequestEntity.builder().build();
        }finally {
            lock.unlock();
        }
    }

    private int getValueWithRandomIndex(){
        lock.lock();
        try{
            Random random = new Random();
            return listValues.size() != 1 ? random.nextInt(0,listValues.size()-1) : 0;
        }finally {
            lock.unlock();
        }
    }

    private void countAccumulator(){
       accumulator = futureList.stream()
               .mapToInt(x -> {
                    try {
                        return x.get().getValue();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sum();
    }

    public int getAccumulator() {
        return accumulator;
    }

    public List<Integer> getListValues() {
        return listValues;
    }

    private static List<Integer> initialList(int size){
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < size+1; i++){
            list.add(i);
        }
        return list;
    }
}
