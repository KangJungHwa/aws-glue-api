package com.lgdisplay.bigdata.api.service.glue.controller;

import com.lgdisplay.bigdata.api.service.glue.commands.GlueDefaultRequestCommand;
import com.lgdisplay.bigdata.api.service.glue.util.StopWatch;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang.StringUtils.removeEnd;

@Getter
@Setter
@Slf4j
public class RequestContext {

    HttpServletRequest request;
    HttpServletResponse response;
    String body;
    String requestId;
    StopWatch stopWatch;
    String username;
    String commandName;
    String region;
    String endpoint;

    public RequestContext(HttpServletRequest request, HttpServletResponse response, String body, String requestId, String command, String region) {
        this.request = request;
        this.response = response;
        this.body = body;
        this.requestId = requestId;
        this.stopWatch = new StopWatch(requestId);
        this.commandName = command;
        this.username = GlueDefaultRequestCommand.getUsername(request);
        this.region = region;

        // TODO : Endpoint URL이 IP가 아니라 DNS이고 Client와 다르다면 이 부분은 파라미터로 받아야 함
        String url = request.getRequestURL().toString();
        this.endpoint = url.endsWith("/") ? removeEnd(url, "/") : url;
    }

    public void startStopWatch(String taskName) throws IllegalStateException {
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }
        log.info("{} 요청의 '{}' 작업을 시작합니다.", commandName, taskName);
        stopWatch.start(taskName);
    }

    public void startStopWatch(String taskName, String format, Object... args) throws IllegalStateException {
        if (stopWatch.isRunning()) {
            stopWatch.stop();
        }
        String message = String.format("%s 요청의 '%s' 작업을 시작합니다. %s", commandName, taskName, String.format(format, args));
        log.info("{}", message);
        stopWatch.start(taskName);
    }

    public void stopStopWatch() throws IllegalStateException {
        if (stopWatch.isRunning()) stopWatch.stop();
    }

    public String stopStopWatchAndSummary() throws IllegalStateException {
        if (stopWatch.isRunning()) stopWatch.stop();
        return stopWatch.prettyPrint();
    }
}