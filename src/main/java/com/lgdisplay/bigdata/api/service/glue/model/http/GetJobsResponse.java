package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lgdisplay.bigdata.api.service.glue.model.http.Job;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetJobsResponse {

    @JsonProperty("Jobs")
    private List<Job> jobs;

    @JsonProperty("NextToken")
    private String nextToken;
}
