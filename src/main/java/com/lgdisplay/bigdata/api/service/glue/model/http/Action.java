package com.lgdisplay.bigdata.api.service.glue.model.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {

    @JsonProperty("Arguments")
    private Map<String,String> arguments;

    @JsonProperty("CrawlerName")
    private String crawlerName;

    @JsonProperty("JobName")
    private String jobName;

    @JsonProperty("NotificationProperty")
    private NotificationProperty notificationProperty;

    @JsonProperty("SecurityConfiguration")
    private String securityConfiguration;

    @JsonProperty("TimeOut")
    private Integer timeOut;

}
