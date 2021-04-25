package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
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
        String jobName = startJobRunRequest.getJobName();
        String jobRunId = startJobRunRequest.getJobRunId(); // Retry시 사용
        Map<String, String> arguments = startJobRunRequest.getArguments();

        StartJobRunResponse errorResponse = StartJobRunResponse.builder().build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            // InvalidInputException
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<Job> byUsernameAndJobName = jobRepository.findByUsernameAndJobName(context.getUsername(), jobName);
        if (!byUsernameAndJobName.isPresent()) {
            // EntityNotFoundException
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("Job 정보 생성 및 실행");

        String generatedJobRunId = "JOB_" + System.currentTimeMillis();

        context.getLogging().setResourceName(jobName);
        context.getLogging().setJobRunId(generatedJobRunId);

        String jobSchedulerUrl = resourceService.getJobScheduler();

        HashMap params = new HashMap();
        params.put("username", context.getUsername());
        params.put("scriptLocation", byUsernameAndJobName.get().getScriptLocation());
        params.put("scriptName", byUsernameAndJobName.get().getScriptName());
        params.put("jobId", generatedJobRunId);
        params.put("arguments", arguments);
        params.put("jobName", jobName);

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