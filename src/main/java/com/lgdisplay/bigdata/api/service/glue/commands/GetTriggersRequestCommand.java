package com.lgdisplay.bigdata.api.service.glue.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetJobsResponse;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetTriggersRequest;
import com.lgdisplay.bigdata.api.service.glue.model.http.GetTriggersResponse;
import com.lgdisplay.bigdata.api.service.glue.repository.JobRepository;
import com.lgdisplay.bigdata.api.service.glue.repository.TriggerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class GetTriggersRequestCommand extends GlueDefaultRequestCommand implements GlueRequestCommand {

    @Autowired
    TriggerRepository triggerRepository;

    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Override
    public String getName() {
        return "AWSGlue.GetTriggers";
    }

    @Override
    public ResponseEntity execute(RequestContext context) throws Exception {
        GetTriggersRequest getTriggersRequest = mapper.readValue(context.getBody(), GetTriggersRequest.class);
        Integer maxResults = getTriggersRequest.getMaxResults();

        context.startStopWatch("모든 Trigger 조회");

        GetTriggersResponse response = new GetTriggersResponse();

        Iterable<Trigger> triggers = null;
        PageRequest pageRequest = PageRequest.of(0, maxResults);
        if (maxResults != null) {
            if (getTriggersRequest.getDependentJobName() != null) {
                triggers = triggerRepository.findAllByNameLimitN(getTriggersRequest.getDependentJobName(), pageRequest);
            }else {
                triggers = triggerRepository.findAllLimitN(pageRequest);
            }
        }else {
            if (getTriggersRequest.getDependentJobName() != null) {
                triggers = triggerRepository.findAll();
            }else{
                triggers = triggerRepository.findListByName(getTriggersRequest.getDependentJobName());
            }
        }

        if (triggers == null) {
            // EntityNotFoundException
            return ResponseEntity.badRequest().build();
        }

        context.startStopWatch("GetTriggers 결과 반환");

        List<com.lgdisplay.bigdata.api.service.glue.model.http.Trigger> selectedTriggers = new ArrayList();
        for (Trigger trigger : triggers) {
            selectedTriggers.add(mapper.readValue(trigger.getBody(), com.lgdisplay.bigdata.api.service.glue.model.http.Trigger.class));
        }

        response.setTriggers(selectedTriggers);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean authorize(RequestContext context) throws Exception {
        return true;
    }
}
