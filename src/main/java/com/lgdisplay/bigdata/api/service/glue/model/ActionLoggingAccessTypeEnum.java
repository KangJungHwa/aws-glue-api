package com.lgdisplay.bigdata.api.service.glue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionLoggingAccessTypeEnum implements BaseEnumCode<String> {

    START_JOB("START_JOB"),
    KILL_JOB("KILL_JOB");

    private final String value;

}
