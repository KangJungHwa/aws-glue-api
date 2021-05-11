package com.lgdisplay.bigdata.api.service.glue.controller;

import com.lgdisplay.bigdata.api.service.glue.commands.GlueRequestCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GlueRequestDispatcher implements InitializingBean, ApplicationContextAware {

    /**
     * Request Command Map
     */
    Map<String, GlueRequestCommand> commandMap;

    /**
     * Request Command
     */
    List<GlueRequestCommand> commands;

    /**
     * Glue API Context Path
     */
    String glueContextPath;

    public GlueRequestCommand getCommand(HttpServletRequest request, String body) {
        String actionName = request.getHeader("X-Amz-Target");

        if (actionName == null) {
            throw new UnsupportedOperationException("지원하지 않는 기능입니다.");
        }
        return commandMap.get(actionName);
    }

    public void setCommands(List<GlueRequestCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (commands == null) {
            throw new IllegalArgumentException("GLUE Command를 지정해 주십시오.");
        }

        // Spring이 초기화 되면 Command Key와 Command Impl의 매핑 정보를 생성한다.
        this.commandMap = new HashMap<>();
        this.commands.forEach(command -> {
            commandMap.put(command.getName(), command);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Environment env = applicationContext.getBean(Environment.class);
        this.glueContextPath = env.getProperty("app.service-context-paths.glue");
    }
}
