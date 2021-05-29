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
        String userName = context.getUsername().toUpperCase();
        String jobName = createJobRequest.getName().toUpperCase();
        String scriptName = createJobRequest.getCommand().getName();
        String scriptLocation = createJobRequest.getCommand().getScriptLocation();

        CreateJobResponse response = CreateJobResponse.builder().name(jobName).build();

        context.startStopWatch("사용자의 Job Name NULL 여부 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("scriptType의 유효성 확인");
        if(!scriptTypeList.contains(createJobRequest.getCommand().getName())){
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job Name Unique 유효성 확인");

        Optional<Job> optionalJob = jobRepository.findByJobName(jobName);
        if (optionalJob.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job 저장");

        context.getLogging().setResourceName(jobName);

        // 별로 Primary Key를 관리하지만 기본은 username과 job name은 unique 해야 한다.
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

        context.startStopWatch("CreateJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
