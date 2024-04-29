package com.paintogain.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.exception.handler.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class Http401Handler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public Http401Handler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[인가 오류] 로그인 필요");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus("401")
                .message("[인가 오류] 로그인 필요")
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(UTF_8.displayName());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(json);

    }
}
