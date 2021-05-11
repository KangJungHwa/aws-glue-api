package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteTriggerRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteTriggerResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public class DeleteTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    TriggerRepository triggerRepository;

    @Override
    public String getName() {
        return "AWSGlue.DeleteTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        DeleteTriggerRequest deleteTriggerRequest = mapper.readValue(context.getBody(), DeleteTriggerRequest.class);
        String name = deleteTriggerRequest.getName();

        DeleteTriggerResponse response = DeleteTriggerResponse.builder().name(name).build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger Name 유효성 확인");

        Optional<Trigger> byName = triggerRepository.findByName(name);
        if (!byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job 삭제");

        context.getLogging().setResourceName(name);

        Trigger trigger = Trigger.builder()
                .triggerId(byName.get().getTriggerId())
                .build();

        triggerRepository.delete(trigger);

        context.startStopWatch("DeleteTrigger 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
