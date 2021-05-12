package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListTriggersRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.ListTriggersResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListTriggersRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.ListTriggers";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        ListTriggersRequest listTriggersRequest = mapper.readValue(context.getBody(), ListTriggersRequest.class);
        Integer maxResults = listTriggersRequest.getMaxResults();

        context.startStopWatch("사용자의 모든 Trigger 조회");
//        List<String> triggerNames=triggerRepository.findListAllTriggers();
        List<String> triggerNames= null;
        PageRequest pageRequest = PageRequest.of(0, maxResults);
        if (maxResults != null) {
            if (listTriggersRequest.getDependentJobName() != null) {
                triggerNames = triggerRepository.findListTriggersLimitN(listTriggersRequest.getDependentJobName(), pageRequest);
            }else {
                triggerNames = triggerRepository.findListAllTriggersLimitN(pageRequest);
            }
        }else {
            if (listTriggersRequest.getDependentJobName() != null) {
                triggerNames = triggerRepository.findListTriggers(listTriggersRequest.getDependentJobName());
            }else{
                triggerNames = triggerRepository.findListAllTriggers();
            }
        }

        if (triggerNames == null) {
            // EntityNotFoundException
            return ResponseEntity.badRequest().build();
        }

        context.startStopWatch("ListTriggers 결과 반환");

        ListTriggersResponse response = ListTriggersResponse.builder().triggerNames(triggerNames).build();
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
