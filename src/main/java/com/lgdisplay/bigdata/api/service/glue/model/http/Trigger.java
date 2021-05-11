package com.lgdisplay.bigdata.api.service.glue.model.http;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trigger {

    @JsonProperty("Actions")
    private List<Action> actions;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Predicate")
    private Predicate predicate;

    @JsonProperty("Schedule")
    private String schedule;

    @JsonProperty("State")
    private String state;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("WorkflowName")
    private String workflowName;
}
