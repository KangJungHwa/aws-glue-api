package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public class GetJobRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    JobRepository jobRepository;

    @Override
    public String getName() {
        return "AWSGlue.GetJob";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetJobRequest getJobRequest = mapper.readValue(context.getBody(), GetJobRequest.class);
        String jobName = getJobRequest.getJobName();

        GetJobResponse errorResponse = GetJobResponse
                .builder()
                .job(com.lgdisplay.bigdata.api.service.glue.model.http.Job.builder().name(jobName).build())
                .build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<Job> byUsernameAndJobName = jobRepository.findByJobName(jobName);
        if (!byUsernameAndJobName.isPresent()) {
            return ResponseEntity.status(400).body(errorResponse);
        }

        context.startStopWatch("Job 정보 확인");

        context.getLogging().setResourceName(jobName);

        Job job = byUsernameAndJobName.get();
        com.lgdisplay.bigdata.api.service.glue.model.http.Job finalResponseJob =
                mapper.readValue(job.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.Job.class);
        errorResponse.setJob(finalResponseJob);

        context.startStopWatch("GetJob 결과 반환");

        return ResponseEntity.ok(errorResponse);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
