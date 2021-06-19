package com.lgdisplay.bigdata.api.service.glue.service;

import com.lgdisplay.bigdata.api.service.glue.model.ServerStatus;
import com.lgdisplay.bigdata.api.service.glue.repository.ServerHealthRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * API Server 및 Job Scheduler 서버의 리소스를 확인하여 리소스 사용을 결정하는 서비스.
 */
@Slf4j
@Service
public class ResourceService {

    @Autowired
    ServerHealthRepository serverHealthRepository;

    @Value("${app.scheduler-port}")
    String schedulerPort;

    // 리소스 사용량이 적은 서버를 반환합니다.
    public String getTargetServer(){
       Optional<ServerStatus> optionalServerStatus= serverHealthRepository.findTargetServer();
        if (!optionalServerStatus.isPresent()) {
             log.error("Target Server Not Found!");
             return "ERROR";
        }
        ServerStatus serverStatus= optionalServerStatus.get();
        return "http://"+serverStatus.getIpAddress();
    }
    public String getJobStartUrl() {
        return getTargetServer()+":"+schedulerPort+"/service/scheduler/start/job";
    }
    public String getJobUrl() {
        return getTargetServer()+":"+schedulerPort+"/service/scheduler/job";
    }
    public String getTriggerUrl() {
        return getTargetServer()+":"+schedulerPort+"/service/scheduler/trigger";
    }

    // 사용자 z driver를 공용 storage에 반환합니다.
    public String copyPublicStorage(String userName)  {
        try {
            //TODO 배포시 아래경로 리눅스 스타일로 변경할 것
            String sourceDirectoryLocation = "C:/mnt/" + userName + "/Documents";
            //TODO 아래부분의 경로는 확정되면 수정할 것
            String destinationDirectoryLocation = "C:/DEV/" + userName + "/Documents";


//            rsync -avzhP test-directory/ /tmp
            String[] cmd = new String[]{"rsync", "-avzhP", "--include", "'**/someDir/*MG*'", "--include", "'*/'", "--exclude", "'*'",
                    "-e", "ssh -i /home/localUser/.ssh/id_rsa -l ec2-user",
                    "remoteUser@ip.address.net:/home/remoteUser/baseDir/", "/home/localUser/testing"};

            File sourceDirectory = new File(sourceDirectoryLocation);
            File destinationDirectory = new File(destinationDirectoryLocation);
            FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
            return "OK";
        } catch (IOException e) {
            log.error(e.toString());
            return "ERROR";
        }
    }
}
