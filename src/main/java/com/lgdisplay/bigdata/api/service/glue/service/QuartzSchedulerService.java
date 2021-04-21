package com.lgdisplay.bigdata.api.service.glue.service;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzSchedulerService {

    @Autowired
    Scheduler scheduler;

}
