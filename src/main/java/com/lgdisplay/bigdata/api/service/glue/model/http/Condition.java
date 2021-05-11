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
public class Condition {
    @JsonProperty("CrawlerName")
    private String crawlerName;

    @JsonProperty("CrawlState")
    private String crawlerState;

    @JsonProperty("JobName")
    private String jobName;

    @JsonProperty("LogicalOperator")
    private String logicalOperator;

    @JsonProperty("State")
    private String state;
}
