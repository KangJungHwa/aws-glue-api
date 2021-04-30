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
        String jobName = getJobRunsRequest.getJobName();

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
            runs = runRepository.findAllLimitN(jobName, pageRequest);
        } else {
            runs = runRepository.findAll();
        }

        if (runs == null) {
            // EntityNotFoundException
            return ResponseEntity.badRequest().build();
        }

        context.startStopWatch("GetJobRuns 결과 반환");

        List<com.lgdisplay.bigdata.api.service.glue.model.http.JobRuns> selectedRuns = new ArrayList();
        for (com.lgdisplay.bigdata.api.service.glue.model.Run run : runs) {
            JobRuns jobRuns = mapper.readValue(run.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.JobRuns.class);
            jobRuns.setId(run.getJobRunId());
            jobRuns.setJobRunState(run.getJobRunState());
            selectedRuns.add(jobRuns);
        }
        JobRuns jr = selectedRuns.get(0);
        System.out.println("1------------"+ jr.getJobRunState());
        response.setJobRuns(selectedRuns);
        response.setNextToken("");


        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
