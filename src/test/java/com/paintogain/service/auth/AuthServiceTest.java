package com.paintogain.service.auth;

import com.paintogain.controller.auth.request.Login;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.domain.User;
import com.paintogain.exception.custom.AlreadyExistsEmailException;
import com.paintogain.exception.custom.InvalidSigninInformation;
import com.paintogain.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
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

        @Test
        @DisplayName("로그인 성공")
        void login1() {
            // given
            var user = User.builder()
                    .email("sol@gmail.com")
                    .password(passwordEncoder.encode("1234"))
                    .name("sol")
                    .build();
            userRepository.save(user);

            var login = Login.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();

            // when
            Long userId = authService.signin(login);

            assertThat(userId).isEqualTo(1L);
        }

        @Test
        @DisplayName("로그인 실패 - 패스워드 틀림")
        void login2() {
            // given
            var user = User.builder()
                    .email("sol@gmail.com")
                    .password(passwordEncoder.encode("1234"))
                    .name("sol")
                    .build();
            userRepository.save(user);

            var login = Login.builder()
                    .email("sol@gmail.com")
                    .password("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                    .build();

            // expected
            assertThatThrownBy(() -> authService.signin(login))
                    .isInstanceOf(InvalidSigninInformation.class)
                    .hasMessage("아이디/비밀번호가 올바르지 않습니다");
        }

    }
}