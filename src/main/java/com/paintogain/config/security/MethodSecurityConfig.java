package com.paintogain.config.security;

import com.paintogain.repository.feed.FeedRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

    private final FeedRepository feedRepository;

    public MethodSecurityConfig(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator(feedRepository));
        return handler;
    }
}
