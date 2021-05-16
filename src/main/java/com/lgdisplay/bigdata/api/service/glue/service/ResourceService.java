package com.lgdisplay.bigdata.api.service.glue.service;

import com.lgdisplay.bigdata.api.service.glue.model.ServerHealth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * API Server 및 Job Scheduler 서버의 리소스를 확인하여 리소스 사용을 결정하는 서비스.
 */
@Service
public class ResourceService {

    @Value("${app.scheduler-urls}")
    List<String> jobSchedulers;

    /**
     * API Server의 Health 정보
     */
    private ServerHealth apiServerHealth;

    /**
     * API Server의 Health 정보를 업데이트한다.
     *
     * @param apiServerHealth API Server의 Health
     */
    public void setApiServerHealth(ServerHealth apiServerHealth) {
        this.apiServerHealth = apiServerHealth;
    }

    /**
     * API Server의 Health 정보를 반환한다.
     */
    public ServerHealth getApiServerHealth() {
        return apiServerHealth;
    }


    public String getJobStartUrl() {
        return "http://localhost:8889/service/scheduler/start/job";
    }
    public String getJobUrl() {
        return "http://localhost:8889/service/scheduler/job";
    }
    public String getTriggerUrl() {
        return "http://localhost:8889/service/scheduler/trigger";
    }
}
