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
public class GetTriggersResponse {

    @JsonProperty("Triggers")
    private List<Trigger> triggers;

    @JsonProperty("NextToken")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String nextToken;

}
