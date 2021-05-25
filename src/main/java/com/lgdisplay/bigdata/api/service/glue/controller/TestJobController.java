package com.lgdisplay.bigdata.api.service.glue.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.StartJobRunRequest;
import com.lgdisplay.bigdata.api.service.glue.repository.RunRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/glue/scheduler")
@Slf4j


public class TestJobController extends DefaultController {

    @Autowired
    RunRepository runRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;


    @PostMapping("/startJobRun")
    public ResponseEntity startJobRun(@RequestBody Map params) throws Exception  {
        log.info("{}", params);
        String jobRunId=params.get("jobRunId").toString();
        StartJobRunRequest startJobRunRequest = mapper.readValue(params.get("body").toString(), StartJobRunRequest.class);

        Run startJobRun = Run.builder()
                .jobRunId(jobRunId)
                .jobName(startJobRunRequest.getJobName())
                .jobRunState("START").build();
        runRepository.save(startJobRun);
        //TODO
        // 아래에서 JOB 실행시키고 STATE 코드 RUNNING FINISH 업데이트 시키는 로직추가
        return ResponseEntity.ok(jobRunId);
    }

}
