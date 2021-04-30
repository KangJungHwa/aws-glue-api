package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetJobRunRequest {

    @JsonProperty("JobName")
    private String jobName;

    @JsonProperty("RunId")
    private String runId;

    @JsonProperty("PredecessorsIncluded")
    private boolean predecessorsIncluded;


}
