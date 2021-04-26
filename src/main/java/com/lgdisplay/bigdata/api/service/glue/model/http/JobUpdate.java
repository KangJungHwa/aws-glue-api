package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class JobUpdate {

    @JsonProperty("Command")
    private JobCommand jobCommand;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("LogUri")
    private String logUri;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("ExecutionProperty")
    private ExecutionProperty executionProperty;

    @JsonProperty("DefaultArguments")
    private Map<String, String> defaultArguments;

    @JsonProperty("NonOverridableArguments")
    private Map<String, String> nonOverridableArguments;

    @JsonProperty("Connections")
    private ConnectionsList connections;

    @JsonProperty("MaxRetries")
    private Integer maxRetries;

    @JsonProperty("AllocatedCapacity")
    private Integer allocatedCapacity;

    @JsonProperty("Timeout")
    private Integer timeout;

    @JsonProperty("MaxCapacity")
    private Double maxCapacity;

    @JsonProperty("SecurityConfiguration")
    private String securityConfiguration;

    @JsonProperty("Tags")
    private Map<String, String> tags;

    @JsonProperty("NotificationProperty")
    private NotificationProperty notificationProperty;

    @JsonProperty("GlueVersion")
    private String glueVersion;

    @JsonProperty("NumberOfWorkers")
    private Integer numberOfWorkers;

    @JsonProperty("WorkerType")
    private String workerType;
}
