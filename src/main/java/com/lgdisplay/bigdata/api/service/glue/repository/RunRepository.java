package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;

import org.springframework.data.repository.CrudRepository;


import java.util.List;
import java.util.Optional;

public interface RunRepository extends CrudRepository<Run, String> {

    Optional<Run> findByJobName(String jobName);
}
