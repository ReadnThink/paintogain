package com.paintogain.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.controller.auth.request.Login;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.domain.Session;
import com.paintogain.domain.User;
import com.paintogain.repository.SessionRepository;
import com.paintogain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Nested
    class 인증 {
        @Test
        void 로그인_성공() throws Exception {
            // given
            userRepository.save(User.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build());

            Login build = Login.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();

            String json = objectMapper.writeValueAsString(build);

            //when
            mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Transactional
        @Test
        void 로그인_성공_후_세션_1개_생성() throws Exception {
            // given
            User user = User.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();
            userRepository.save(user);

            Login login = Login.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();

            //when
            mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

            assertThat(1L).isEqualTo(user.getSessions().size());
        }

        @Test
        void 로그인_성공_후_세션_응답() throws Exception {
            // given
            User user = User.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();
            userRepository.save(user);

            Login build = Login.builder()
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();

            String json = objectMapper.writeValueAsString(build);

            //when
            mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(json)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").isNotEmpty())
                    .andDo(print());
        }

        @Test
        void 로그인_성공_후_권한이필요한_페이지접속() throws Exception {
            // given
            User user = User.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();
            Session session = user.addSession();

            userRepository.save(user);

            //when
            mockMvc.perform(get("/test")
                            .header("Authorization", session.getAccessToken())
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void 로그인_성공_후_검증되지않은세션_접속불가() throws Exception {
            // given
            User user = User.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();
            Session session = user.addSession();

            userRepository.save(user);

            //when
            mockMvc.perform(get("/test")
                            .header("Authorization", session.getAccessToken() + " Error ! ")
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andDo(print());
        }

    }

    @Nested
    class 회원가입 {

        @Test
        @DisplayName("회원가입 성공")
        void signIn() throws Exception {
            // given
            var signup = Signup.builder()
                    .name("Test")
                    .email("sol@gmail.com")
                    .password("1234")
                    .build();

            //when
            mockMvc.perform(post("/auth/signup")
                            .content(objectMapper.writeValueAsString(signup))
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

}