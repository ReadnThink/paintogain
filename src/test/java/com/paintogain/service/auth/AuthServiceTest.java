package com.paintogain.service.auth;

import com.paintogain.controller.auth.request.Signup;
import com.paintogain.domain.User;
import com.paintogain.exception.custom.AlreadyExistsEmailException;
import com.paintogain.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Nested
    class 회원가입 {
        @Test
        @DisplayName("회원가입 성공")
        void test1() {
            // given
            var signup = Signup.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .name("sol")
                    .build();

            // when
            authService.signup(signup);

            // then
            assertThat(1).isEqualTo(userRepository.count());

            User user = userRepository.findAll().get(0);
            assertThat(user.getEmail()).isEqualTo("sol@gmail.com");
            assertThat(user.getName()).isEqualTo("sol");
            assertTrue(passwordEncoder.matches("1234", user.getPassword()));
        }

        @Test
        @DisplayName("회원가입 실패 - 중복 이메일")
        void test2() {
            // given
            var signup = Signup.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .name("sol")
                    .build();

            var user = User.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .name("sol")
                    .build();
            userRepository.save(user);

            // expected
            assertThatThrownBy(() -> authService.signup(signup))
                    .isInstanceOf(AlreadyExistsEmailException.class)
                    .hasMessage("이미 존재하는 이메일입니다");
        }

    }

    @Nested
    class 로그인 {

    }
}