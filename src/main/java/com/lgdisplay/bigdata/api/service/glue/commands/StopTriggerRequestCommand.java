package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerStateEnum;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartTriggerRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartTriggerResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.StopTriggerRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StopTriggerResponse;
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
public class StopTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

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
        return "AWSGlue.StopTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {

        StopTriggerRequest stopJobRunRequest = mapper.readValue(context.getBody(), StopTriggerRequest.class);

        String name = stopJobRunRequest.getName();
        String userName = context.getUsername().toUpperCase();
        StopTriggerResponse response = StopTriggerResponse.builder().build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            // InvalidInputException
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger Name 존재여부 확인");

        Optional<Trigger> optionalTrigger = triggerRepository.findByUserNameAndName(userName,name);
        Trigger trigger = optionalTrigger.get();
        if (!optionalTrigger.isPresent()) {
            // EntityNotFoundException
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger  running 여부 확인");
        if (!trigger.getTriggerState().equals(TriggerStateEnum.RUNNING.name())) {
            log.error(trigger.getName()+" :is  not running ");
            return ResponseEntity.status(400).body(response);
        }


        context.startStopWatch("Trigger 정지 실행 ");

        context.getLogging().setResourceName(name);

        String triggerUrl = resourceService.getTriggerUrl();



        restTemplate.put(triggerUrl+"/stop/"+trigger.getTriggerId(), trigger.getTriggerId());

        context.getLogging().setJobSchedulerUrl(triggerUrl);
        context.startStopWatch("Stoprigger 결과 반환");
        StopTriggerResponse successResponse = StopTriggerResponse.builder().name(name).build();
        return ResponseEntity.ok(successResponse);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
