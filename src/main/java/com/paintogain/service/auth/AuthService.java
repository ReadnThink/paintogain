package com.paintogain.service.auth;

import com.paintogain.controller.auth.request.Login;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.domain.User;
import com.paintogain.exception.custom.AlreadyExistsEmailException;
import com.paintogain.exception.custom.InvalidSigninInformation;
import com.paintogain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long signin(Login login) {
        var user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new InvalidSigninInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        userRepository.findByEmail(signup.getEmail())
                .ifPresent(user -> {
                    throw new AlreadyExistsEmailException();
                });

        var user = User.builder()
                .name(signup.getName())
                .password(passwordEncoder.encode(signup.getPassword()))
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
