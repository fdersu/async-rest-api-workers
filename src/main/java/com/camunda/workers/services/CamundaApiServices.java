package com.camunda.workers.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class CamundaApiServices {

    @Value("${camunda.bpm.client.base-url}")
    private String camundaApiUrl;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    public void loadJsonVariable(String processInstanceId, String variableName, JsonNode node){
        String apiEndpoint = String.format("/process-instance/%s/variables/%s", processInstanceId, variableName);
        String url = camundaApiUrl + apiEndpoint;
        ObjectNode content = mapper.createObjectNode();
        content.put("value", node.toString());
        content.put("type", "Json");
        RequestEntity<JsonNode> request = RequestEntity.put(url).headers(this.getHeaders()).body(content);
        ResponseEntity<JsonNode> response = null;
        try{
            response = restTemplate.exchange(request, JsonNode.class);
        }catch(Exception e){
            log.error("there was an error pushing camunda variable {} to tha API : {}", variableName, e.getMessage());
        }
        if(response != null && response.hasBody() && response.getStatusCodeValue() == 204){
            log.info("variable {} was successfully pushed to camunda API", variableName);
        }
    }

    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
