package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.CreateJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.CreateJobResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
import com.lgdisplay.bigdata.api.service.glue.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CreateJobRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Value("${glue.script-type-list}")
    List scriptTypeList;

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
        return "AWSGlue.CreateJob";
    }


    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        CreateJobRequest createJobRequest = mapper.readValue(context.getBody(), CreateJobRequest.class);
        String userName = context.getUsername();
        String jobName = createJobRequest.getName();
        String scriptName = createJobRequest.getCommand().getName();
        String scriptLocation = createJobRequest.getCommand().getScriptLocation();

        CreateJobResponse response = CreateJobResponse.builder().name(jobName).build();

        context.startStopWatch("???????????? Job Name NULL ?????? ????????? ??????");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("scriptType??? ????????? ??????");
        if(!scriptTypeList.contains(createJobRequest.getCommand().getName())){
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job Name Unique ????????? ??????");

        Optional<Job> optionalJob = jobRepository.findByJobName(jobName);
        if (optionalJob.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job ??????");

        context.getLogging().setResourceName(jobName);

        // ?????? Primary Key??? ??????????????? ????????? username??? job name??? unique ?????? ??????.
        Job createJob = Job.builder()
                .jobId(System.currentTimeMillis())
                .jobName(jobName)
                .username(userName)
                .scriptName(scriptName)
                .scriptLocation(scriptLocation)
                .body(context.getBody()).build();
        jobRepository.save(createJob);

        String jobCreateUrl = resourceService.getJobUrl();

        HashMap params = new HashMap();
        params.put("userName", userName);
        params.put("jobName", jobName);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(jobCreateUrl, params, String.class);
        String schedulerJobId = responseEntity.getBody();
        context.getLogging().setSchedulerJobId(schedulerJobId);
        context.getLogging().setJobSchedulerUrl(jobCreateUrl);

        context.startStopWatch("CreateJob ?????? ??????");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
