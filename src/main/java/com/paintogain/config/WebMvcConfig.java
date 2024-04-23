package com.paintogain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AppConfig appConfig;

    public WebMvcConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

}
