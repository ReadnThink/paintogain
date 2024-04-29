package com.paintogain.controller.auth;

import com.paintogain.config.AppConfig;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;
    public AuthController(AuthService authService, AppConfig appConfig) {
        this.authService = authService;
        this.appConfig = appConfig;
    }

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지입니다.";
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
