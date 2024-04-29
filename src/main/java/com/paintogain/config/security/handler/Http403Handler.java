package com.paintogain.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.exception.handler.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class Http403Handler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public Http403Handler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[인증 오류]");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus("403")
                .message("[인증 오류]")
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(UTF_8.displayName());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }
}
