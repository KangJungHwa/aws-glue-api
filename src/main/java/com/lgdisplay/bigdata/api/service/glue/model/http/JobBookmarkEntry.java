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
public class JobBookmarkEntry {

    @JsonProperty("Attempt")
    private Integer attempt;

    @JsonProperty("JobBookmark")
    private String jobBookmark;

    @JsonProperty("JobName")
    private String jobName;

    @JsonProperty("PreviousRunId")
    private String previousRunId;

    @JsonProperty("Run")
    private Integer run;

    @JsonProperty("RunId")
    private String runId;

    @JsonProperty("Version")
    private Integer version;

}
