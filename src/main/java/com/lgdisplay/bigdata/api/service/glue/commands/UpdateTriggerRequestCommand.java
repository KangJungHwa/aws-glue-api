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
public class UpdateTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.UpdateTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        UpdateTriggerRequest triggerRequest = mapper.readValue(context.getBody(), UpdateTriggerRequest.class);
        List<Action> actionList = triggerRequest.getTriggerUpdate().getActions();
        List<String> jsonList = new ArrayList<>();
        for (Action action:actionList) {
            jsonList.add(mapper.writeValueAsString(action));
        }
        String keyName = triggerRequest.getName();
        String jsonStr = String.join(",", jsonList);
        String updateName = triggerRequest.getTriggerUpdate().getName();
        String schedule = triggerRequest.getTriggerUpdate().getSchedule();
        String description = triggerRequest.getTriggerUpdate().getDescription();

        context.startStopWatch("trigger Name 유효성 확인");

        com.lgdisplay.bigdata.api.service.glue.model.http.Trigger returnTrigger
                = new com.lgdisplay.bigdata.api.service.glue.model.http.Trigger();
        returnTrigger.setName(updateName);
        UpdateTriggerResponse response = UpdateTriggerResponse.builder()
                .trigger(returnTrigger)
                .build();

        if (StringUtils.isEmpty(keyName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 trigger Name 유효성 확인");

        Optional<Trigger> byName = triggerRepository.findByName(keyName);
        if (!byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        Trigger trigger = byName.get();

        context.startStopWatch("Job 수정");

        String body=context.getBody();
        body=body.replace("\"TriggerUpdate\":{\"Name\":\""+keyName+"\",", "")
                .replace(",\"Predicate\":{}}}", "");
        body=body+",\"StartOnCreation\":"+trigger.getStartOnCreate();
        body=body+",\"WorkflowName\":\""+trigger.getWorkflowName()+"\"";
        body=body+",\"Type\":\""+trigger.getType()+"\"";
        body=body+"}";
        trigger.setTriggerId(trigger.getTriggerId());
        trigger.setName(updateName);
        trigger.setSchedule(schedule);
        trigger.setDescription(description);
        trigger.setBody(body);

        triggerRepository.save(trigger);


        context.startStopWatch("UpdateJob 결과 반환");

        returnTrigger.setActions(actionList);
        returnTrigger.setName(updateName);
        returnTrigger.setDescription(description);
        returnTrigger.setSchedule(schedule);
        returnTrigger.setType(trigger.getType());
        returnTrigger.setWorkflowName(trigger.getWorkflowName());
        returnTrigger.setId(trigger.getTriggerId());


        response.builder()
                .trigger(returnTrigger)
                .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
