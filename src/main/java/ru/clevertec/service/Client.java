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

public class Client {
    private final ExecutorService executorService;
    private List<Integer> listValues;
    private List<Future<RequestResponse>> futureList;
    private int accumulator;

    public Client(int requestCounts) {
        executorService = Executors.newFixedThreadPool(5);
        listValues = initialList(requestCounts);
        futureList = new ArrayList<>();
    }

    public Client(int requestCounts, int threadPool) {
        executorService = Executors.newFixedThreadPool(threadPool);
        listValues = initialList(requestCounts);
        futureList = new ArrayList<>();
    }

    public void processingResponse(Server server){
        while (listValues.size() > 0){
            futureList.add(sendRequestToServer(server,(RequestEntity) getRandomEntityForRequest()));
        }
        executorService.shutdown();
        countAccumulator();
    }

    private Future<RequestResponse> sendRequestToServer(Server server, RequestEntity request){
        return executorService.submit(
                () -> server.processingRequest(request)
        );
    }

    private RequestResponse getRandomEntityForRequest(){
        RequestResponse request;
        if(!listValues.isEmpty()){
            request = RequestEntity.builder()
                    .requestValue(listValues.remove(getValueWithRandomIndex()))
                    .build();
            return request;
        }else return RequestEntity.builder().build();
    }

    private int getValueWithRandomIndex(){
        Random random = new Random();
        return listValues.size() != 1 ? random.nextInt(0,listValues.size()-1) : 0;
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
