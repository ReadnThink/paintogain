package com.paintogain.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.controller.auth.request.Signup;
import com.paintogain.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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


    @AfterEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    class 인증 {

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