package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.UpdateJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.UpdateJobResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public class UpdateJobRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.UpdateJob";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        UpdateJobRequest JobRequest = mapper.readValue(context.getBody(), UpdateJobRequest.class);
        String jobName = JobRequest.getJobName();
        String scriptName = JobRequest.getJobUpdate().getJobCommand().getName();
        String scriptLocation = JobRequest.getJobUpdate().getJobCommand().getScriptLocation();

        UpdateJobResponse response = UpdateJobResponse.builder().jobName(jobName).build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<Job> byUsernameAndJobName = jobRepository.findByUsernameAndJobName(context.getUsername(), jobName);
        if (!byUsernameAndJobName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        Job job = byUsernameAndJobName.get();
        Long jobId = job.getJobId();

        context.startStopWatch("Job 수정");

        String body=context.getBody();
        body=body.replace("JobName", "Name")
                  .replace("\"JobUpdate\":{", "")
                  .replace("}}}", "}}");

        Job updateJob = Job.builder()
                .jobId(jobId)
                .jobName(jobName)
                .username(context.getUsername())
                .scriptName(scriptName)
                .scriptLocation(scriptLocation)
                .body(body).build();
        jobRepository.save(updateJob);

        context.startStopWatch("UpdateJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
