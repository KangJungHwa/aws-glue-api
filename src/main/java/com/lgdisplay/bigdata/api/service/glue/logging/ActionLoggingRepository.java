package com.lgdisplay.bigdata.api.service.glue.logging;

import com.lgdisplay.bigdata.api.service.glue.model.ActionLogging;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLoggingRepository extends CrudRepository<ActionLogging, Long> {
}
