package com.lgdisplay.bigdata.api.service.glue.service;

import com.lgdisplay.bigdata.api.service.glue.model.ServerHealth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job Scheduler 서버의 리소스를 확인하여 최적의 Job Scheduler를 결정하는 서비스.
 */
@Service
public class ResourceService {

    @Value("${app.scheduler-urls}")
    List<String> jobSchedulers;

    private ServerHealth apiServerHealth;

    public void setApiServerHealth(ServerHealth apiServerHealth) {
        this.apiServerHealth = apiServerHealth;
    }

    public ServerHealth getApiServerHealth() {
        return apiServerHealth;
    }

}
