package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerStateEnum;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerTypeEnum;
import com.lgdisplay.bigdata.api.service.glue.model.http.*;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CreateTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getName() {
        return "AWSGlue.CreateTrigger";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        CreateTriggerRequest createTriggerRequest = mapper.readValue(context.getBody(), CreateTriggerRequest.class);
        List<String> jobNames= new ArrayList<>();

        String workflowName = createTriggerRequest.getWorkflowName();
        String name = createTriggerRequest.getName();
        String schedule = createTriggerRequest.getSchedule();
        String description = createTriggerRequest.getDescription();
        String type = createTriggerRequest.getType();
        Boolean start_on_creation = createTriggerRequest.getStartOnCreation();
        String userName=context.getUsername();
        String jobName=createTriggerRequest.getActions().get(0).getJobName();
        CreateTriggerResponse response = CreateTriggerResponse.builder().name(name).build();

        context.startStopWatch("Trigger Name null ?????? ????????? ??????");

        //name, type??? ?????? ?????? ????????? ????????? ????????????.
        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("Trigger Type null ?????? ????????? ??????");
        if(StringUtils.isEmpty(type)){
                return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Trigger Type??? SCHEDULED??? ?????? cron ????????? ????????? ??????");
        //type SCHEDULED ????????? cron Expression??? ????????? ????????? ??????.
        if(type.equals(TriggerTypeEnum.SCHEDULED.name())) {
            if (StringUtils.isEmpty(schedule)){
                return ResponseEntity.status(400).body(response);
            }
        }
        //ON_DEMAND ????????? startOnCreation??? true ?????? ??????.
        //ON_DEMAND ????????? ???????????? ????????? ??? ??????.
        if(type.equals(TriggerTypeEnum.ON_DEMAND.name())) {
            context.startStopWatch("Trigger Type??? ON_DEMAND??? ?????? start_on_creation ??? ????????? ??????");
            if (start_on_creation){
                return ResponseEntity.status(400).body(response);
            }
            context.startStopWatch("Trigger Type??? ON_DEMAND??? ?????? cron ????????? null??? ?????? ?????? ??????");
            if(!StringUtils.isEmpty(schedule)){
                return ResponseEntity.status(400).body(response);
            }
        }
        //trigger Action??? ?????? job??? job ???????????? ?????? ?????? ?????? ??????.
        context.startStopWatch("Trigger??? ????????? Job Name??? Job ???????????? ??????????????? ????????? ??????");
        for (Action action:createTriggerRequest.getActions()) {
            jobNames.add(action.getJobName());
        }
        List<Job> returnJobList=jobRepository.findByJobNameIn(jobNames);
        if (returnJobList.size() != jobNames.size()) {
            return ResponseEntity.status(400).body(response);
        }
        //trigger ?????? unique ?????? ??????.
        context.startStopWatch("???????????? Trigger Name Unique ????????? ??????");
        Optional<Trigger> byName = triggerRepository.findByName(name);
        if (byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Trigger ??????");
        context.getLogging().setResourceName(name);

        String triggerState= (start_on_creation) ? TriggerStateEnum.STARTED.name():TriggerStateEnum.STANDBY.name();
        Trigger createTrigger = Trigger.builder()
                .triggerId(String.valueOf(System.currentTimeMillis()))
                .name(name)
                .workflowName(workflowName)
                .schedule(schedule)
                .description(description)
                .startOnCreate(start_on_creation)
                .description(description)
                .type(type)
                .triggerState(triggerState)
                .userName(userName)
                .jobName(jobName)
                .body(context.getBody()).build();

        triggerRepository.save(createTrigger);
        if(start_on_creation==true) {
            String triggerCreateUrl = resourceService.getTriggerUrl();
            System.out.println("triggerCreateUrl"+triggerCreateUrl);
            HashMap params = new HashMap();
            params.put("userName", userName);
            params.put("triggerName", name);

            // glue??? ????????? ???????????? ???????????? jobName??? ????????? ??? ?????????
            // quartz??? ????????? ?????? ????????? ????????????.
            // ??????????????? triggerName?????? glue ???????????? ???????????? ????????????.
            params.put("jobName", jobName);
            //TODO ?????? ?????? ?????? copyPublicStorage ????????? ????????? ?????? prefix ????????? ???
            //resourceService.copyPublicStorage(userName);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(triggerCreateUrl, params, String.class);
            String schedulerTriggerId = responseEntity.getBody();
            context.getLogging().setSchedulerJobId(schedulerTriggerId);
            context.getLogging().setJobSchedulerUrl(triggerCreateUrl);
            context.startStopWatch("CreateTrigger ?????? ??????");

            //???????????? running?????? ??????????????? ?????? ??????
            Optional<Trigger> trigger=triggerRepository.findByUserNameAndName(userName,name);
            //????????? ????????? ?????????.
            if (!trigger.isPresent()) {
                return ResponseEntity.status(400).body(response);
            }

            Trigger updateTrigger=trigger.get();
            updateTrigger.setTriggerState(TriggerStateEnum.RUNNING.name());
            triggerRepository.save(updateTrigger);

        }
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
