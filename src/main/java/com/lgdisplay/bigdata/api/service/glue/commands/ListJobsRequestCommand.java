package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListJobsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.ListJobs";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        ListJobsRequest listJobsRequest = mapper.readValue(context.getBody(), ListJobsRequest.class);
        Integer maxResults = listJobsRequest.getMaxResults();
        String username = context.getUsername();

        context.startStopWatch("모든 Job 조회");

        Iterable<Job> jobs = null;
        if (maxResults != null) {
            PageRequest pageRequest = PageRequest.of(0, maxResults);
            jobs = jobRepository.findAllLimitN(username, pageRequest);
        } else {
            jobs = jobRepository.findAll();
        }

        context.startStopWatch("ListJobs 결과 반환");

        List<String> jobNames = new ArrayList();
        jobs.forEach(job -> {
            jobNames.add(job.getJobName());
        });

        ListJobsResponse response = ListJobsResponse.builder().jobNames(jobNames).build();
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
