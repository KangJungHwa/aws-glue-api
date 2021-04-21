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
                .serviceType("IAM")
                .ipAddress(request.getRemoteHost())
                .requestId(requestId)
                .username(context.getUsername()).build();
        actionLoggingService.startUsage(logging);

        try {
            MDC.put("requestId", context.getRequestId());
            MDC.put("invocationId", request.getHeader("amz-sdk-invocation-id"));

            log.debug("Request의 Header 정보: \n{}", HttpRequestDebugUtils.headerToString(request));

            log.info("{} 요청에 대해서 처리를 시작합니다.", command.getName());

            // TODO : uncomment
            // if (!command.authorize(context)) throw new IllegalArgumentException("해당 사용자를 인증할수 없습니다.");

            ResponseEntity result = command.execute(context);

            if (result.getStatusCodeValue() >= 400) {
                logging.setStatus(ActionLoggingStatusTypeEnum.FAILED);
                logging.setMessage(result.getBody().toString());
            } else {
                logging.setStatus(ActionLoggingStatusTypeEnum.SUCCEEDED);
            }

            logging.setElapsedTime(System.currentTimeMillis() - requestId);
            logging.setHttpStatus(result.getStatusCodeValue());
            actionLoggingService.endUsage(logging, null);

            return result;
        } catch (Exception e) {
            logging.setStatus(ActionLoggingStatusTypeEnum.FAILED);
            logging.setElapsedTime(System.currentTimeMillis() - requestId);
            actionLoggingService.endUsage(logging, e);

            log.warn("{} 요청이 에러가 발생하여 500 에러로 처리합니다.\n{}", command.getName(), ExceptionUtils.getFullStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } finally {
            if (context.getStopWatch().isRunning()) {
                context.getStopWatch().stop();
            }
            log.debug("{} 요청에 대한 처리를 완료하였습니다. 요청의 각 단계별 처리 현황은 다음과 같습니다.\n{}", command.getName(), context.getStopWatch().prettyPrint());
            MDC.clear();
        }
    }

}