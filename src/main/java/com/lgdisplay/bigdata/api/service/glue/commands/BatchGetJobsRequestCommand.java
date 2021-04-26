package com.lgdisplay.bigdata.api.service.glue.commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.http.*;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.util.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BatchGetJobsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Override
    public String getName() {
        return "AWSGlue.BatchGetJobs";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {

        ObjectMapper mapper = (ObjectMapper) ApplicationContextHolder.get().getBean("mapper");
        BatchGetJobsRequest getJobRequest = mapper.readValue(context.getBody(), BatchGetJobsRequest.class);

        BatchGetJobsResponse response = new BatchGetJobsResponse();
        //TODO
        // 사용자의 유효성 확인
        //        context.startStopWatch("사용자 유효성 확인");
        // context.startStopWatch("사용자의 Job Name 유효성 확인");

        List<com.lgdisplay.bigdata.api.service.glue.model.Job> byJobname = jobRepository.findByJobNameIn(getJobRequest.getJobNames());


        List<Job> resJobList =
                new ArrayList<>();
        for (int i = 0; i <byJobname.size(); i++) {
            com.lgdisplay.bigdata.api.service.glue.model.Job job=byJobname.get(i);
            Job finalResponseJob =
                    mapper.readValue(job.getBody(), Job.class);
            resJobList.add(finalResponseJob);
        }


        response.setJobs(resJobList);
//        response.setJobsNotFound("");
        context.startStopWatch("Job 조회");
        context.startStopWatch("BatchGetJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
