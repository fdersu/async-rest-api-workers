package com.camunda.workers;

import com.camunda.workers.services.AsyncRestApiServices;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@ExternalTaskSubscription("signal-end-worker")
public class SignalEndWorker implements ExternalTaskHandler {

    @Autowired
    AsyncRestApiServices asyncRestApiServices;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> vars = new HashMap<>();
        log.info("sending end workflow signal to API for workflow {}", externalTask.getProcessInstanceId());
        try {
            asyncRestApiServices.signalWorkflowEnded(externalTask.getProcessInstanceId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        vars.put("signal", "OK");
        externalTaskService.complete(externalTask, vars);
    }
}
