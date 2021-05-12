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
public class GetTriggersRequest {

    @JsonProperty("DependentJobName")
    private String dependentJobName;

    @JsonProperty("NextToken")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String nextToken;

    @JsonProperty("MaxResults")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Integer maxResults;
}
