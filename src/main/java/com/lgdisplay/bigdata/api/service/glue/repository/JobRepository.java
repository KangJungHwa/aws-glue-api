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
}
