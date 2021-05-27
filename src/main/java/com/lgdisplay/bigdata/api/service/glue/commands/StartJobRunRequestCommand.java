package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.RunRepository;
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
public class StartJobRunRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    RunRepository runRepository;

    @Autowired
    ResourceService resourceService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getName() {
        return "AWSGlue.StartJobRun";
    }


    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {


        StartJobRunRequest startJobRunRequest = mapper.readValue(context.getBody(), StartJobRunRequest.class);
        String jobName = startJobRunRequest.getJobName().toUpperCase();

        Map<String, String> arguments = startJobRunRequest.getArguments();
        String userName=context.getUsername().toUpperCase();
        StartJobRunResponse errorResponse = StartJobRunResponse.builder().build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            // InvalidInputException
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("Job Name 유효성 확인");

        Optional<Job> byUsernameAndJobName = jobRepository.findByJobName(jobName);
        if (!byUsernameAndJobName.isPresent()) {
            // EntityNotFoundException
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("Job 정보 생성 및 실행");

        String generatedJobRunId = "JOB_" + System.currentTimeMillis();

        context.getLogging().setResourceName(jobName);
        context.getLogging().setJobRunId(generatedJobRunId);

        String jobSchedulerUrl = resourceService.getJobStartUrl();

        HashMap params = new HashMap();
        params.put("arguments", MapUtils.mapToJson(arguments));
        params.put("jobName", jobName);
        params.put("userName", byUsernameAndJobName.get().getUsername());
        params.put("body", context.getBody());

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(jobSchedulerUrl, params, String.class);
        String schedulerJobId = responseEntity.getBody();
        context.getLogging().setSchedulerJobId(schedulerJobId);
        context.getLogging().setJobSchedulerUrl(jobSchedulerUrl);
        context.startStopWatch("StartJobRun 결과 반환");
        StartJobRunResponse successResponse = StartJobRunResponse.builder().jobRunId(generatedJobRunId).build();
        return ResponseEntity.ok(successResponse);
    }


    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
