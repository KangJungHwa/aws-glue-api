package com.lgdisplay.bigdata.api.service.glue.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "api_glue_trigger")
public class Trigger {


    @Id
    @Column(name = "trigger_id", columnDefinition = "VARCHAR(255)")
    String triggerId;

    @Column(name = "name",columnDefinition = "VARCHAR(255)")
    String name;

    @Column(name = "description", columnDefinition = "VARCHAR(255)", nullable = true)
    private String description;

    @Column(name = "schedule", columnDefinition = "VARCHAR(255)")
    private String schedule;

    @Column(name = "start_on_create")
    private Boolean startOnCreate;

    @Column(name = "type", columnDefinition = "VARCHAR(255)")
    private String type;

    @Column(name = "workflow_name", columnDefinition = "VARCHAR(255)")
    private String workflowName;

    @Column(name = "job_name", columnDefinition = "VARCHAR(255)")
    private String jobName;

    @Column(name = "userName", columnDefinition = "VARCHAR(255)")
    private String userName;

    @Column(name = "trigger_state", columnDefinition = "VARCHAR(255)")
    private String triggerState;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "body", nullable = true)
    String body;

}
