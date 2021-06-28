package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerStateEnum;
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
    JobRepository jobRepository;


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
        //step1 업데이트 될 context의 body를  UpdateTriggerRequest로 변환한다.
        //step2 UpdateTriggerRequest에서 각 항목을 추출한다.
        //step3 db에 저장된 body를 createTriggerRequest로 변환한다.
        //step4 createTriggerRequest에 각 추출항목을 세팅하고 저장을 한다.
        List<Action> actionList = triggerRequest.getTriggerUpdate().getActions();
        String keyName = triggerRequest.getName();
        String schedule = triggerRequest.getTriggerUpdate().getSchedule();
        String description = triggerRequest.getTriggerUpdate().getDescription();

        com.lgdisplay.bigdata.api.service.glue.model.http.Trigger returnTrigger
                = new com.lgdisplay.bigdata.api.service.glue.model.http.Trigger();
        returnTrigger.setName(keyName);
        UpdateTriggerResponse response = UpdateTriggerResponse.builder()
                .trigger(returnTrigger)
                .build();

        context.startStopWatch("Trigger Name의 null 여부 확인");
        if (StringUtils.isEmpty(keyName)) {
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch(" trigger Name 존재 유무 확인");

        Optional<Trigger> optionalTrigger = triggerRepository.findByName(keyName);
        if (!optionalTrigger.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        Trigger trigger =optionalTrigger.get();
        context.startStopWatch("사용자의 Trigger  running 여부 확인");

        if (trigger.getTriggerState().equals(TriggerStateEnum.RUNNING.name())) {
            log.error(trigger.getName()+" :is running ");
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("update 후 JobName 존재 유무 유효성 확인");
        for (Action action:actionList) {
            Optional<Job> optionalJob = jobRepository.findByJobName(action.getJobName());
            if (!optionalJob.isPresent()) {
                return ResponseEntity.status(400).body(response);
            }
        }


        //저장될 body 생성
        CreateTriggerRequest createTriggerRequest = mapper.readValue(trigger.getBody(), CreateTriggerRequest.class);
        createTriggerRequest.setName(keyName);
        createTriggerRequest.setSchedule(schedule);
        createTriggerRequest.setDescription(description);
        createTriggerRequest.setActions(actionList);
        createTriggerRequest.setWorkflowName(trigger.getWorkflowName());
        String body=mapper.writeValueAsString(createTriggerRequest);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~body~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+body);
        trigger.setTriggerId(trigger.getTriggerId());
        trigger.setName(keyName);
        trigger.setSchedule(schedule);
        trigger.setDescription(description);
        trigger.setBody(body);

        triggerRepository.save(trigger);


        context.startStopWatch("UpdateJob 결과 반환");

        returnTrigger.setActions(actionList);
        returnTrigger.setName(keyName);
        returnTrigger.setDescription(description);
        returnTrigger.setSchedule(schedule);
        returnTrigger.setType(trigger.getType());
        returnTrigger.setWorkflowName(trigger.getWorkflowName());
        returnTrigger.setId(trigger.getTriggerId());


        UpdateTriggerResponse.builder()
                .trigger(returnTrigger)
                .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
