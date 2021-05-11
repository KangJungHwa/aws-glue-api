package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.amazonaws.services.glue.model.JobBookmarkEntry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetJobBookmarkResponse {

    @JsonProperty("JobBookmarkEntry")
    private JobBookmarkEntry jobBookmarkEntry;
}
