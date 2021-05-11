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
public class UpdateTriggerRequest {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("TriggerUpdate")
    private TriggerUpdate triggerUpdate;

}
