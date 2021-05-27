package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.*;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class GetTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    TriggerRepository triggerRepository;

    @Override
    public String getName() {
        return "AWSGlue.GetTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetTriggerRequest getTriggerRequest = mapper.readValue(context.getBody(), GetTriggerRequest.class);
        String name = getTriggerRequest.getName();
        Trigger returnTrigger = new Trigger();
        returnTrigger.setName(name);
        GetTriggerResponse response = GetTriggerResponse
                .builder()
                .trigger(returnTrigger)
                .build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger Name 유효성 확인");

        Optional<com.lgdisplay.bigdata.api.service.glue.model.Trigger> byName = triggerRepository.findByName(name);
        if (!byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Trigger 정보 확인");

        context.getLogging().setResourceName(name);

        com.lgdisplay.bigdata.api.service.glue.model.Trigger trigger = byName.get();


        returnTrigger =
                mapper.readValue(trigger.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.Trigger.class);

        response.setTrigger(returnTrigger);

        context.startStopWatch("GetJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
