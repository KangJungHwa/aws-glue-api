package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
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
        String userName=context.getUsername().toUpperCase();
        String jobName=createTriggerRequest.getActions().get(0).getJobName().toUpperCase();
        CreateTriggerResponse response = CreateTriggerResponse.builder().name(name).build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("사용자의 Trigger Name 유효성 확인");
        for (Action action:createTriggerRequest.getActions()) {
            jobNames.add(action.getJobName());
        }

        List<Job> returnJobList=jobRepository.findByJobNameIn(jobNames);
        if (returnJobList.size() != jobNames.size()) {
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("사용자의 Trigger Name Unique 유효성 확인");
        Optional<Trigger> byName = triggerRepository.findByName(name);
        if (byName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("Trigger 저장");
        context.getLogging().setResourceName(name);

        // 별로 Primary Key를 관리하지만 기본은 username과 job name은 unique 해야 한다.
        String triggerState= (start_on_creation) ? "STARTED":"STANDBY";
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

            HashMap params = new HashMap();
            params.put("userName", userName);
            params.put("triggerName", name);

            // glue는 하나의 스케줄에 여러개의 jobName을 실행할 수 있는데
            // quartz에 맞추기 위해 하나만 입력한다.
            // 실행시에는 triggerName으로 glue 테이블에 조회해서 실행한다.
            params.put("jobName", jobName);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(triggerCreateUrl, params, String.class);
            String schedulerTriggerId = responseEntity.getBody();
            context.getLogging().setSchedulerJobId(schedulerTriggerId);
            context.getLogging().setJobSchedulerUrl(triggerCreateUrl);

            context.startStopWatch("CreateTrigger 결과 반환");
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
