package com.camunda.workers.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AsyncRestApiServices {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> signalWorkflowEnded(String processId){
        String url = String.format("http://localhost:8081/asyncApi/ended-workflows/%s", processId);
        RequestEntity<Void> request = RequestEntity.get(url).headers(this.getHeaders()).build();
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(request, String.class);
        }catch(Exception e){
            log.error("call to signal workflow {} has ended failed", processId);
            return new ResponseEntity<>(String.format("call to signal workflow %s has ended failed", processId), HttpStatus.EXPECTATION_FAILED);
        }
        return response;
    }

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
