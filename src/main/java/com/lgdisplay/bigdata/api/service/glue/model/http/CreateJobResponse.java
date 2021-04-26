package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateJobResponse {

    @JsonProperty("Name")
    private String name;
}
