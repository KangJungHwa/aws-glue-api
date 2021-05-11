package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.http.*;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CreateTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    TriggerRepository triggerRepository;

    @Override
    public String getName() {
        return "AWSGlue.CreateTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        CreateTriggerRequest createTriggerRequest = mapper.readValue(context.getBody(), CreateTriggerRequest.class);
        List<Action> actionList = createTriggerRequest.getActions();
        List<String> jsonList = new ArrayList<>();
        for (Action action:actionList) {
            jsonList.add(mapper.writeValueAsString(action));
        }
        String jsonStr = String.join(",", jsonList);
        String workflowName = createTriggerRequest.getWorkflowName();
        String name = createTriggerRequest.getName();
        String schedule = createTriggerRequest.getSchedule();
        String description = createTriggerRequest.getDescription();
        String type = createTriggerRequest.getType();
        Boolean start_on_creation = createTriggerRequest.getStartOnCreation();

        CreateTriggerResponse response = CreateTriggerResponse.builder().name(name).build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger Name 유효성 확인");

        Optional<Trigger> byName = triggerRepository.findByName(name);
        if (byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Trigger 저장");

        context.getLogging().setResourceName(name);

        // 별로 Primary Key를 관리하지만 기본은 username과 job name은 unique 해야 한다.
        Trigger createTrigger = Trigger.builder()
                .triggerId(String.valueOf(System.currentTimeMillis()))
                .name(name)
                .workflowName(workflowName)
                .schedule(schedule)
                .description(description)
                .startOnCreate(start_on_creation)
                .actions(jsonStr)
                .description(description)
                .type(type)
                .body(context.getBody()).build();

        triggerRepository.save(createTrigger);

        context.startStopWatch("CreateTrigger 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
