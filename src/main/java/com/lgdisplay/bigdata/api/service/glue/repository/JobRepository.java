package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends CrudRepository<Job, Long> {

    Optional<Job> findByUsernameAndJobName(String username, String jobName);
    // GetJobs에서 조회결과를 limit를 줘서 조회하는 기능을 구현해야 함.
    List<Job> findByUsername(String username);
}
