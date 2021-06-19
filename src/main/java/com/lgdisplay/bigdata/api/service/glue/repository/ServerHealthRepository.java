package com.lgdisplay.bigdata.api.service.glue.repository;

import com.lgdisplay.bigdata.api.service.glue.model.ServerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServerHealthRepository extends JpaRepository<ServerStatus, String> {

    @Query(value= "select * \n" +
            "  from api_glue_server_status a,\n" +
            "       (select max(available_memory) as available_max_memory\n" +
            "         from api_glue_server_status) b\n" +
            "  where available_memory = b.available_max_memory ",
            nativeQuery = true)
    Optional<ServerStatus> findTargetServer();
}
