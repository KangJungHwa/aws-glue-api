package com.lgdisplay.bigdata.api.service.glue.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
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

    @PostMapping("/startJobRun")
    public ResponseEntity startJobRun(@RequestBody Map params) {
        log.info("{}", params);
        return ResponseEntity.ok("SCH_JOB_" + RandomUtils.nextInt());
    }

}
