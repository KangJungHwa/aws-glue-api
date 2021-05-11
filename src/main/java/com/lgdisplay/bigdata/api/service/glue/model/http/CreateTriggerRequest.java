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
public class CreateTriggerRequest {

    @JsonProperty("Actions")
    private List<Action> actions;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Predicate")
    private Predicate predicate;

    @JsonProperty("Schedule")
    private String schedule;

    @JsonProperty("StartOnCreation")
    private Boolean startOnCreation;

    @JsonProperty("Tags")
    private Map<String,String> tags;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("WorkflowName")
    private String workflowName;
}
