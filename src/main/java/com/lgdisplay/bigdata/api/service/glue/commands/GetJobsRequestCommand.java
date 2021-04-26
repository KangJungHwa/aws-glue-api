package com.lgdisplay.bigdata.api.service.glue.commands;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.http.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.JobCommand;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.util.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GetJobsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Override
    public String getName() {
        return "AWSGlue.GetJobs";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        ObjectMapper mapper = (ObjectMapper) ApplicationContextHolder.get().getBean("mapper");
        GetJobsRequest getJobRequest = mapper.readValue(context.getBody(), GetJobsRequest.class);

        GetJobsResponse response = new GetJobsResponse();
        //TODO
        // 사용자의 유효성 확인
        //        context.startStopWatch("사용자 유효성 확인");
        // context.startStopWatch("사용자의 Job Name 유효성 확인");

        Optional<List<com.lgdisplay.bigdata.api.service.glue.model.Job>> jobsByUsername = jobRepository.findJobsByUsername(context.getUsername());

//        if (!byUsername.isPresent()) {
//            return ResponseEntity.status(200).body(response);
//        }



//            com.lgdisplay.bigdata.api.service.glue.model.http.Job finalResponseJob =
//                    mapper.readValue(byUsername, List<com.lgdisplay.bigdata.api.service.glue.model.http.Job.class>);

//        ObjectMapper mapper2 = new ObjectMapper();
//        List<com.lgdisplay.bigdata.api.service.glue.model.http.Job> resJobList
//                = mapper2.convertValue(byUsername , new TypeReference<List<com.lgdisplay.bigdata.api.service.glue.model.http.Job>>() {});

        List<com.lgdisplay.bigdata.api.service.glue.model.http.Job> resJobList = new ArrayList<>();
//        for (int i = 0; i <byUsername.size(); i++) {
//            com.lgdisplay.bigdata.api.service.glue.model.Job job=byUsername.get(i);
//            com.lgdisplay.bigdata.api.service.glue.model.http.Job finalResponseJob =
//                    mapper.readValue(job.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.Job.class);
//            resJobList.add(finalResponseJob);
//        }


        response.setJobs(resJobList);
        response.setNextToken("\t");
        context.startStopWatch("Job 조회");
        context.startStopWatch("GetJob 결과 반환");

        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
