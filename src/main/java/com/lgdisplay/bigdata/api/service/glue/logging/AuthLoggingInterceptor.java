package com.lgdisplay.bigdata.api.service.glue.logging;

import com.lgdisplay.bigdata.api.service.glue.util.ExceptionUtils;
import com.lgdisplay.bigdata.api.service.glue.util.StopWatch;
import com.lgdisplay.bigdata.api.service.glue.util.StopWatchAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = String.valueOf(System.currentTimeMillis());
        StopWatch stopWatch = new StopWatch(requestId);
        StopWatchAdaptor adaptor = new StopWatchAdaptor(stopWatch);
        request.setAttribute("stopWatch", adaptor);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String path = builder.build().getPath();
        stopWatch.start("서비스 요청 시작");

        MDC.put("requestId", requestId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatchAdaptor stopWatch = (StopWatchAdaptor) request.getAttribute("stopWatch");
        if (stopWatch.isRunning()) stopWatch.stop();

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();

        if (ex != null) {
            log.warn("{}\n{}", "[" + builder.build().getPath() + "] 인증 관련 기능을 수행하던 도중 에러가 발생했습니다.", ExceptionUtils.getFullStackTrace(ex));
        } else {
            log.info("[" + builder.build().getPath() + "] 인증 관련 기능을 정상적으로 처리하였습니다.");
        }

        log.info("인증 서비스의 처리를 완료하였습니다. 요청의 각 단계별 처리 현황은 다음과 같습니다.\n{}", stopWatch.prettyPrint());

        MDC.clear();
    }
}
