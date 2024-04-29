package com.paintogain.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.exception.handler.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class LoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public LoginFailHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("[인증오류] 아이디/비밀번호 오류");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus("400")
                .message("아이디/비밀번호가 일치하지 않습니다")
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(UTF_8.displayName());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }
}
