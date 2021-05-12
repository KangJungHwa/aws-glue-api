package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TriggerRepository extends JpaRepository<Trigger, String> {
    Optional<Trigger> findByName(String name);

    List<Trigger> findListByName(String name);

    @Query(value = "SELECT t.name FROM api_glue_trigger t WHERE t.name = :name", nativeQuery = true)
    List<String> findListTriggers(@Param("name") String name);

    @Query(value = "SELECT t.name FROM api_glue_trigger t WHERE t.name = :name", nativeQuery = true)
    List<String> findListTriggersLimitN(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT name FROM api_glue_trigger", nativeQuery = true)
    List<String> findListAllTriggers();

    @Query(value = "SELECT name FROM api_glue_trigger", nativeQuery = true)
    List<String> findListAllTriggersLimitN(Pageable pageable);



    @Query("FROM com.lgdisplay.bigdata.api.service.glue.model.Trigger t WHERE t.name = :name ")
    List<Trigger> findAllByNameLimitN(@Param("name") String name, Pageable pageable);

    @Query("FROM com.lgdisplay.bigdata.api.service.glue.model.Trigger t ")
    List<Trigger> findAllLimitN(Pageable pageable);
}
