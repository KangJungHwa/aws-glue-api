package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JobCommand {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ScriptLocation")
    private String scriptLocation;

    @JsonProperty("PythonVersion")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String pythonVersion;
}
