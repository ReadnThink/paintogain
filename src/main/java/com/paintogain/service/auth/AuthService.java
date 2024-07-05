package com.paintogain.service.auth;

import com.paintogain.controller.auth.request.Signup;
import com.paintogain.domain.User;
import com.paintogain.exception.custom.AlreadyExistsEmailException;
import com.paintogain.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository
            , PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(Signup signup) {
        userRepository.findByEmail(signup.getEmail())
                .ifPresent(user -> {
                    throw new AlreadyExistsEmailException();
                });

        var user = User.builder()
                .name(signup.getName())
                .password(
                        passwordEncoder.encode(
                                signup.getPassword()
                        )
                )
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
