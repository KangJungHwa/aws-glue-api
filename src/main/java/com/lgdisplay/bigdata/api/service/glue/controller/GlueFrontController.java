package com.lgdisplay.bigdata.api.service.glue.controller;

import com.lgdisplay.bigdata.api.service.glue.commands.GlueRequestCommand;
import com.lgdisplay.bigdata.api.service.glue.logging.ActionLoggingService;
import com.lgdisplay.bigdata.api.service.glue.model.ActionLogging;
import com.lgdisplay.bigdata.api.service.glue.model.ActionLoggingStatusTypeEnum;
import com.lgdisplay.bigdata.api.service.glue.util.ExceptionUtils;
import com.lgdisplay.bigdata.api.service.glue.util.HttpRequestDebugUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping("/glue")
public class GlueFrontController {

    @Value("${app.region-name}")
    String region;

    @Autowired
    GlueRequestDispatcher requestDispatcher;

    @Autowired
    ActionLoggingService actionLoggingService;

    @RequestMapping(method = {GET, POST, PUT, DELETE, PATCH})
    public ResponseEntity service(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {

        Long requestId = System.currentTimeMillis();
        response.setHeader("x-amzn-RequestId", String.valueOf(requestId));
        GlueRequestCommand command = requestDispatcher.getCommand(request, body);

        RequestContext context = new RequestContext(request, response, body, String.valueOf(requestId), command.getName(), region);

        ActionLogging logging = ActionLogging.builder()
                .actionName(command.getName())
                .serviceType("GLUE")
                .ipAddress(request.getRemoteHost())
                .requestId(requestId)
                .username(context.getUsername()).build();
        actionLoggingService.startUsage(logging);

        context.setLogging(logging);

        try {
            MDC.put("requestId", context.getRequestId());
            MDC.put("invocationId", request.getHeader("amz-sdk-invocation-id"));

            log.debug("Request??? Header ??????: \n{}", HttpRequestDebugUtils.headerToString(request));

            log.info("{} ????????? ????????? ????????? ???????????????.", command.getName());

            if (!command.authorize(context)) throw new IllegalArgumentException("?????? ???????????? ???????????? ????????????.");

            ResponseEntity result = command.execute(context);

            if (result.getStatusCodeValue() >= 400) {
                logging.setStatus(ActionLoggingStatusTypeEnum.FAILED);
                logging.setMessage(result.getBody().toString());
            } else {
                logging.setStatus(ActionLoggingStatusTypeEnum.SUCCEEDED);
            }

            logging.setElapsedTime(System.currentTimeMillis() - requestId);
            logging.setHttpStatus(result.getStatusCodeValue());
            logging.setLayerResponseTime(context.getStopWatch().prettyJson());
            actionLoggingService.endUsage(logging, null);

            return result;
        } catch (Exception e) {
            logging.setStatus(ActionLoggingStatusTypeEnum.FAILED);
            logging.setElapsedTime(System.currentTimeMillis() - requestId);
            logging.setLayerResponseTime(context.getStopWatch().prettyJson());
            actionLoggingService.endUsage(logging, e);

            log.warn("{} ????????? ????????? ???????????? 500 ????????? ???????????????.\n{}", command.getName(), ExceptionUtils.getFullStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            if (context.getStopWatch().isRunning()) {
                context.getStopWatch().stop();
            }
            log.debug("{} ????????? ?????? ????????? ?????????????????????. ????????? ??? ????????? ?????? ????????? ????????? ????????????.\n{}", command.getName(), context.getStopWatch().prettyPrint());
            MDC.clear();
        }
    }

}