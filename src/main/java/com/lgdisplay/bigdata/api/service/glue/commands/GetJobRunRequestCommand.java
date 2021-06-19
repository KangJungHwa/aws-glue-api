package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobRunRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobRunResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.JobRuns;
import com.lgdisplay.bigdata.api.service.glue.repository.RunRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
public class GetJobRunRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    RunRepository RunRepository;

    @Override
    public String getName() {
        return "AWSGlue.GetJobRun";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetJobRunRequest getJobRunRequest = mapper.readValue(context.getBody(), GetJobRunRequest.class);
        String jobName = getJobRunRequest.getJobName();
        String runId = getJobRunRequest.getRunId();
        GetJobRunResponse response = GetJobRunResponse
                .builder().jobRuns(JobRuns.builder().jobName(jobName).build())
                .build();

        context.startStopWatch("Job Name 유효성 확인");

        if (StringUtils.isEmpty(jobName)) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<Run> byJobNameAndJobRunId = RunRepository.findByJobNameAndJobRunId(jobName,runId);

        if (!byJobNameAndJobRunId.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("GetJobRun 정보 확인");

        context.getLogging().setResourceName(jobName);

        Run run = byJobNameAndJobRunId.get();

        JobRuns jobRuns = new JobRuns();
        jobRuns.setJobName(jobName);
        jobRuns.setId(runId);
        jobRuns.setJobRunState(run.getJobRunState());
        jobRuns.setId(run.getTriggerId());
        response.setJobRuns(jobRuns);
        context.startStopWatch("GetJobRun 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
