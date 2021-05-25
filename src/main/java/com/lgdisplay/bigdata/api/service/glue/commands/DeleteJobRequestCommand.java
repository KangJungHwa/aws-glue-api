package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.DeleteJobResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
public class DeleteJobRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    JobRepository jobRepository;

    @Override
    public String getName() {
        return "AWSGlue.DeleteJob";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        DeleteJobRequest deleteJobRequest = mapper.readValue(context.getBody(), DeleteJobRequest.class);
        String jobName = deleteJobRequest.getJobName().toUpperCase();
        String userName=context.getUsername().toUpperCase();

        DeleteJobResponse response = DeleteJobResponse.builder().jobName(jobName).build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<Job> byUsernameAndJobName = jobRepository.findByUsernameAndJobName(userName, jobName);
        if (!byUsernameAndJobName.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("Job 삭제");

        context.getLogging().setResourceName(jobName);

        Job job = Job.builder()
                .jobId(byUsernameAndJobName.get().getJobId())
                .build();
        jobRepository.delete(job);

        context.startStopWatch("DeleteJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
