package com.lgdisplay.bigdata.api.service.glue.logging;

import com.lgdisplay.bigdata.api.service.glue.model.ActionLogging;

public interface ActionLoggingService {

    void startUsage(ActionLogging logging);

    void endUsage(ActionLogging logging, Exception e);

}
