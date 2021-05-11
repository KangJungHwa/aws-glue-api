package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.Job;
import com.lgdisplay.bigdata.api.service.glue.model.Run;
import com.lgdisplay.bigdata.api.service.glue.model.Trigger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TriggerRepository extends CrudRepository<Trigger, String> {
    Optional<Trigger> findByName(String name);
}
