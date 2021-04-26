package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.util.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class ListJobsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Override
    public String getName() {
        return "AWSGlue.ListJobs";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        ObjectMapper mapper = (ObjectMapper) ApplicationContextHolder.get().getBean("mapper");
        ListJobsRequest getJobRequest = mapper.readValue(context.getBody(), ListJobsRequest.class);

        ListJobsResponse response = new ListJobsResponse();
        //TODO
        // 사용자의 유효성 확인
        //        context.startStopWatch("사용자 유효성 확인");
        // context.startStopWatch("사용자의 Job Name 유효성 확인");

        List<Job> byUsername = jobRepository.findByUsername(context.getUsername());

//        if (!byUsername.isPresent()) {
//            return ResponseEntity.status(200).body(response);
//        }

       // List<Job> job=byUsername;
        Stream<Job> stream = byUsername.stream();
        stream.forEach(job ->{
            List<String> jobList = new ArrayList<>();
            jobList.add(job.getJobName());
            response.setJobNames(jobList);
        });

        context.startStopWatch("사용자별 Job명 조회");
        context.startStopWatch("List Job 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
