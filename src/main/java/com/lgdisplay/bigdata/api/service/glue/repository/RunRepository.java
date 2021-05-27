package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface RunRepository extends CrudRepository<Run, String> {

    @Query("FROM com.lgdisplay.bigdata.api.service.glue.model.Run j WHERE j.userName = :userName and j.jobName = :jobName")
    List<Run> findByUserNameAndJobNameLimitN(@Param("userName") String userName, @Param("jobName") String jobName, Pageable pageable);

    Optional<Run> findByJobNameAndJobRunId(String jobName, String runId);

    Optional<List<Run>> findByJobName(String jobName);

    List<Run> findByUserNameAndJobName(String userName,String jobName);

}
