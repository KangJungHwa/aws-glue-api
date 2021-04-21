package com.lgdisplay.bigdata.api.service.glue.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HealthCheckJob implements Job {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Object bean = ApplicationContextHolder.get().getBean("");
    }

}