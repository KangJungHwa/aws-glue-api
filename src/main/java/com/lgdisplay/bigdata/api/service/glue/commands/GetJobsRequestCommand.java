package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GetJobsRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.GetJobs";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetJobsRequest getJobRequest = mapper.readValue(context.getBody(), GetJobsRequest.class);
        Integer maxResults = getJobRequest.getMaxResults();

        context.startStopWatch("사용자의 모든 Job 조회");

        GetJobsResponse response = new GetJobsResponse();

        Optional<List<com.lgdisplay.bigdata.api.service.glue.model.Job>> byUsername
                = jobRepository.findJobsByUsername(context.getUsername());

        if (!byUsername.isPresent()) {
            return ResponseEntity.status(400).body(response);
        }

        Iterable<Job> jobs = null;
        if (maxResults != null) {
            PageRequest pageRequest = PageRequest.of(0, maxResults);
            jobs = jobRepository.findAllLimitN(pageRequest);
        } else {
            jobs = jobRepository.findAll();
        }

        if (jobs == null) {
            // EntityNotFoundException
            return ResponseEntity.badRequest().build();
        }

        context.startStopWatch("GetJobs 결과 반환");

        List<com.lgdisplay.bigdata.api.service.glue.model.http.Job> selectedJobs = new ArrayList();
        for (Job job : jobs) {
            selectedJobs.add(mapper.readValue(job.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.Job.class));
        }

        response.setJobs(selectedJobs);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
