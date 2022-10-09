package com.camunda.workers;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@ExternalTaskSubscription("pause-worker")
public class PauseWorker implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> vars = new HashMap<>();
        int pause = 1;
        vars.put("pause", pause);
        try {
            log.info("worker pause has stopped the thread for {} seconds", pause);
            TimeUnit.SECONDS.sleep(pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("resuming thread, completing external task");
        externalTaskService.complete(externalTask, vars);
    }
}
