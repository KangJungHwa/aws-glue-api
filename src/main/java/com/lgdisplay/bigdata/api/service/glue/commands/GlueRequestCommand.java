package com.lgdisplay.bigdata.api.service.glue.commands;

import com.lgdisplay.bigdata.api.service.glue.controller.RequestContext;
import org.springframework.http.ResponseEntity;

/**
 * AWS Glue Action을 처리하는 Request Command 인터페이스.
 * 각각의 Action을 처리하는 처리기는 반드시 이 인터페이스를 구현해야 한다.
 */
public interface GlueRequestCommand {

    /**
     * Request Command의 명칭.
     * 이 명칭은 IAM Action명이 되며 이 이름이 잘못 기입되어 있는 경우 Request를 제대로 처리하지 못한다.
     *
     * @return AWS IAM Action명
     */
    String getName();

    /**
     * AWS IAM Action을 처리한다.
     *
     * @param context Request Context
     * @return HTTP Response
     * @throws Exception 예외 발생시
     */
    ResponseEntity execute(RequestContext context) throws Exception;

    /**
     * Authorization Header를 검증한다.
     *
     * @param context Request Context
     * @return 인증 성공시 <tt>true</tt>
     * @throws Exception 인증 예외 발생시
     */
    boolean authorize(RequestContext context) throws Exception;

}
