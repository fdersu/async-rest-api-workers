package com.camunda.workers.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AsyncRestApiServices {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public void signalWorkflowEnded(String processId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        String url = String.format("http://localhost:8081/asyncApi/ended-workflows/%s", processId);
        RequestEntity<Void> request = RequestEntity.get(url).headers(this.getHeaders()).build();
        try {
            restTemplate.exchange(request, String.class);
        }catch(Exception e){
            log.error("call to signal workflow {} has ended failed", processId);
        }
    }

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
