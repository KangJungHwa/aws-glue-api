package com.lgdisplay.bigdata.api.service.glue.logging;

import com.lgdisplay.bigdata.api.service.glue.model.ActionLogging;
import com.lgdisplay.bigdata.api.service.glue.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionLoggingServiceImpl implements ActionLoggingService {

    @Autowired
    ActionLoggingRepository repository;

    @Override
    public void startUsage(ActionLogging logging) {
        repository.save(logging);
    }

    @Override
    public void endUsage(ActionLogging logging, Exception e) {
        logging.setMessage(ExceptionUtils.getFullStackTrace(e));
        repository.save(logging);
    }

}