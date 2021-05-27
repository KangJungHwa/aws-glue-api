package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.http.*;
import com.lgdisplay.bigdata.api.service.glue.repository.RunRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GetJobRunsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    RunRepository runRepository;

    @Override
    public String getName() {
        return "AWSGlue.GetJobRuns";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetJobRunsRequest getJobRunsRequest = mapper.readValue(context.getBody(), GetJobRunsRequest.class);
        Integer maxResults = getJobRunsRequest.getMaxResults();
        String jobName = getJobRunsRequest.getJobName().toUpperCase();
        String userName=context.getUsername().toUpperCase();
        context.startStopWatch("사용자의 모든 Job Run 조회");

        GetJobRunsResponse response = new GetJobRunsResponse();

        Optional<List<Run>> byJobname
                = runRepository.findByJobName(jobName);
        context.startStopWatch("사용자의 Job Name 유효성 확인");

        if (!byJobname.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        context.startStopWatch("GetJobRuns 정보 확인");

        context.getLogging().setResourceName(jobName);

        Iterable<com.lgdisplay.bigdata.api.service.glue.model.Run> runs = null;
        if (maxResults != null) {
            PageRequest pageRequest = PageRequest.of(0, maxResults);
            runs = runRepository.findByUserNameAndJobNameLimitN(userName,jobName, pageRequest);
        } else {
            runs = runRepository.findByUserNameAndJobName(userName,jobName);
        }

        if (runs == null) {
            // EntityNotFoundException
            return ResponseEntity.badRequest().build();
        }

        context.startStopWatch("GetJobRuns 결과 반환");
        List<JobRuns> jobRunList = new ArrayList<>();
        List<com.lgdisplay.bigdata.api.service.glue.model.http.JobRuns> selectedRuns = new ArrayList();
        for (com.lgdisplay.bigdata.api.service.glue.model.Run run : runs) {
            JobRuns jobRuns=JobRuns.builder()
                    .jobName(run.getJobName())
                    .jobRunState(run.getJobRunState())
                    .id(run.getJobRunId())
                    .startedOn(Integer.parseInt(String.valueOf(run.getCreateDate().getTime()/1000)))
                    .executionTime(Long.parseLong(String.valueOf(run.getCreateDate().getTime()-run.getCreateDate().getTime())))
                    .completedOn(Integer.parseInt(String.valueOf(run.getUpdateDate().getTime()/1000)))
                    .triggerName(run.getTriggerName())
                    .build();
            jobRunList.add(jobRuns);
        }
        response.setJobRuns(jobRunList);
        response.setNextToken("");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
