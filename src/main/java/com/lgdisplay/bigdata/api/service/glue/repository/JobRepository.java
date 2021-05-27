package com.lgdisplay.bigdata.api.service.glue.repository;

        import com.lgdisplay.bigdata.api.service.glue.model.Job;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.data.repository.query.Param;

        import java.util.List;
        import java.util.Optional;

public interface JobRepository extends CrudRepository<Job, Long> {

    Optional<Job> findByUsernameAndJobName(String username, String jobName);

    Optional<Job> findByJobName(String jobName);

    @Query("FROM com.lgdisplay.bigdata.api.service.glue.model.Job j ORDER BY j.createDate DESC")
    List<Job> findAllLimitN(Pageable pageable);

    Optional<List<Job>> findJobsByUsername(String username);

    List<Job> findByJobNameIn(List<String> jobname);

    @Query(value= "select COUNT(1) " +
                  "  FROM " +
                  "      (select JSONB_ARRAY_ELEMENTS(body::::jsonb->'Actions')->>'JobName' as job_name  " +
                  "         from api_glue_trigger) A " +
                  " where A.job_name = :jobName",
            nativeQuery = true)
    Integer findUsingJobCountNative(@Param("jobName") String jobName);

    @Query(value= "select COUNT(1) from api_glue_job_run A where A.job_run_state ='RUNNING' and  A.job_name = :jobName",
            nativeQuery = true)
    Integer findRunningJobCountNative(@Param("jobName") String jobName);

    @Query(value= "select COUNT(1) " +
            "  FROM " +
            "      (select JSONB_ARRAY_ELEMENTS(body::::jsonb->'Actions')->>'JobName' as job_name  " +
            "         from api_glue_trigger where trigger_state='RUNNING') A " +
            " where A.job_name = :jobName",
            nativeQuery = true)
    Integer findUsingJobRunningTriggerCountNative(@Param("jobName") String jobName);


}
