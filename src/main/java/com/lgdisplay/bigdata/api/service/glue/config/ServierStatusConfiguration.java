package com.lgdisplay.bigdata.api.service.glue.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
@Configuration
@Slf4j
public class ServierStatusConfiguration {


    @Bean
    public Map<String, String> ServerStatusMap(){
        java.util.Map<String, String> map = new java.util.HashMap<String, String>();
        map.put("Hello", "world");
        return map;
    }
}
