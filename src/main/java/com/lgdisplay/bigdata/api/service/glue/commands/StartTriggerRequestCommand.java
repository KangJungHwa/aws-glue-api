package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.TriggerStateEnum;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartTriggerRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartTriggerResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.RunRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
import com.lgdisplay.bigdata.api.service.glue.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class StartTriggerRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

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
        return "AWSGlue.StartTrigger";
    }


    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {


        StartTriggerRequest startJobRunRequest = mapper.readValue(context.getBody(), StartTriggerRequest.class);
        String name = startJobRunRequest.getName();

        StartTriggerResponse response = StartTriggerResponse.builder().build();

        context.startStopWatch("Trigger Name 유효성 확인");

        if (StringUtils.isEmpty(name)) {
            // InvalidInputException
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Trigger Name 유효성 확인");

        Optional<Trigger> byName = triggerRepository.findByName(name);
        if (!byName.isPresent()) {
            // EntityNotFoundException
            return ResponseEntity.status(400).body(response);
        }
        context.startStopWatch("사용자의 Trigger Name 시작 여부 확인");
        if (byName.get().getTriggerState() == TriggerStateEnum.RUNNING.name()
             || byName.get().getTriggerState() == TriggerStateEnum.STARTED.name()) {
            log.error(byName.get().getName()+" :is  already started ");
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Trigger 실행 ");

        context.getLogging().setResourceName(name);

        String triggerUrl = resourceService.getTriggerUrl();

        HashMap params = new HashMap();

        params.put("triggerName", name);
        params.put("userName", context.getUsername());
        params.put("jobName", byName.get().getJobName());


        ResponseEntity<String> responseEntity = restTemplate.postForEntity(triggerUrl, params, String.class);
        String schedulerTriggerId = responseEntity.getBody();
        context.getLogging().setSchedulerJobId(schedulerTriggerId);
        context.getLogging().setJobSchedulerUrl(triggerUrl);
        context.startStopWatch("StartTrigger 결과 반환");
        StartTriggerResponse successResponse = StartTriggerResponse.builder().name(name).build();
        return ResponseEntity.ok(successResponse);
    }


    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
