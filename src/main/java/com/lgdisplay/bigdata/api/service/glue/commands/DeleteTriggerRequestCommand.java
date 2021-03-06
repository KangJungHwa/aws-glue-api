package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerStateEnum;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteTriggerRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteTriggerResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class DeleteTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public String getName() {
        return "AWSGlue.DeleteTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        DeleteTriggerRequest deleteTriggerRequest = mapper.readValue(context.getBody(), DeleteTriggerRequest.class);

        String userName=context.getUsername();
        String name = deleteTriggerRequest.getName();

        DeleteTriggerResponse response = DeleteTriggerResponse.builder().name(name).build();

        context.startStopWatch("Trigger Name ????????? ??????");

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("???????????? Trigger Name ????????? ??????");

        Optional<Trigger> optionalTrigger = triggerRepository.findByUserNameAndName(userName,name);
        if (!optionalTrigger.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("???????????? Trigger  running ?????? ??????");
        if (optionalTrigger.get().getTriggerState().equals(TriggerStateEnum.RUNNING.name())) {
            log.error(optionalTrigger.get().getName()+" :is running ");
            return ResponseEntity.status(400).body(response);
        }


        context.startStopWatch("Job ??????");

        context.getLogging().setResourceName(name);

        String triggerUrl = resourceService.getTriggerUrl();


        String triggerId=optionalTrigger.get().getTriggerId();
        restTemplate.delete(triggerUrl+"/"+triggerId, triggerId);

        context.getLogging().setJobSchedulerUrl(triggerUrl);


        context.startStopWatch("DeleteTrigger ?????? ??????");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
