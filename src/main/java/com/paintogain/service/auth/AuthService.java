package com.paintogain.service.auth;

import com.paintogain.controller.auth.Login;
import com.paintogain.domain.Session;
import com.paintogain.domain.User;
import com.paintogain.exception.custom.InvalidSigninInformation;
import com.paintogain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String signIn(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        return user.addSession().getAccessToken();
    }
}
