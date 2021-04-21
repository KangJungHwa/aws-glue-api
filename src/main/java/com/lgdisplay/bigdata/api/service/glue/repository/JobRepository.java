package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JobRepository extends CrudRepository<Job, Long> {

    Optional<Job> findByUsernameAndJobName(String username, String jobName);

}
