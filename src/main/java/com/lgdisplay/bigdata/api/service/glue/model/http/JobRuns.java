package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobRuns {

    @JsonProperty("AllocatedCapacity")
    private Integer allocatedCapacity;

    @JsonProperty("Attempts")
    private Map<String,String> attempts;

    @JsonProperty("Attempt")
    private Integer attempt;

    @JsonProperty("CompletedOn")
    private Integer completedOn;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ExecutionTime")
    private Long executionTime;

    @JsonProperty("GlueVersion")
    private String glueVersion;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("JobName")
    private String jobName;

    @JsonProperty("JobRunState")
    private String jobRunState;

    @JsonProperty("LastModifiedOn")
    private Integer lastModifiedOn;

    @JsonProperty("LogGroupName")
    private String logGroupName;

    @JsonProperty("MaxCapacity")
    private Integer maxCapacity;

    @JsonProperty("NotificationProperty")
    private NotificationProperty notificationProperty;

    @JsonProperty("NumberOfWorkers")
    private Integer numberOfWorkers;

    @JsonProperty("PredecessorRuns")
    private List<Predecessor> predecessorRuns;

    @JsonProperty("PreviousRunId")
    private String previousRunId;

    @JsonProperty("SecurityConfiguration")
    private String securityConfiguration;

    @JsonProperty("StartedOn")
    private Integer startedOn;

    @JsonProperty("Timeout")
    private Integer timeout;

    @JsonProperty("TriggerName")
    private String triggerName;

    @JsonProperty("WorkerType")
    private String workerType;


}
