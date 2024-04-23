package com.paintogain.controller.auth;

import com.paintogain.config.AppConfig;
import com.paintogain.controller.auth.request.Login;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.controller.auth.response.SessionResponse;
import com.paintogain.service.auth.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;
    public AuthController(AuthService authService, AppConfig appConfig) {
        this.authService = authService;
        this.appConfig = appConfig;
    }

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        Long userId = authService.signin(login);

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getSecretKey());

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(key)
                .issuedAt(new Date())
                .compact();

        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
