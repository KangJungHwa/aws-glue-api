package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BatchGetJobsResponse {

    @JsonProperty("Jobs")
    private List<Job> jobs;

    @JsonProperty("JobsNotFound")
    private String jobsNotFound;
}
