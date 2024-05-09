package com.paintogain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.annotation.CustomWithMockUser;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.domain.Feed;
import com.paintogain.domain.User;
import com.paintogain.repository.FeedRepository;
import com.paintogain.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void setUp() {
        feedRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class 피드_저장 {
        @Test
        @CustomWithMockUser
        @DisplayName("피드 작성 성공")
        void 피드_DB_저장_성공() throws Exception {
            // given
            FeedCreate feedCreate = FeedCreate.builder()
                    .title("title")
                    .content("Hello world")
                    .build();
            String feedCreateJson = objectMapper.writeValueAsString(feedCreate);

            //when
            mockMvc.perform(post("/feeds")
                            .header("authorization", "sol")
                            .contentType(APPLICATION_JSON)
                            .content(feedCreateJson)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());

            // then
            assertThat(feedRepository.count()).isEqualTo(1L);
            assertThat(feedRepository.findAll().get(0).getTitle()).isEqualTo("title");
        }

        @Test
        @CustomWithMockUser
        @DisplayName("피드 저장 실패 타이틀 Null")
        void 피드_저장_실패_타이틀_Null() throws Exception {
            //given
            FeedCreate feedCreate = FeedCreate.builder()
                    .title(null)
                    .content("Hello world")
                    .build();
            String feedCreateJson = objectMapper.writeValueAsString(feedCreate);

            // when
            mockMvc.perform(post("/feeds")
                            .contentType(APPLICATION_JSON)
                            .content(feedCreateJson)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.httpStatus").value("400"))
                    .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                    .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요"))
                    .andDo(print());
        }

        @Test
        @CustomWithMockUser
        @DisplayName("피드 저장 실패 바보포함")
        void 피드_저장_실패_바보포함() throws Exception {
            //given
            FeedCreate feedCreate = FeedCreate.builder()
                    .title("바보")
                    .content("Hello world")
                    .build();
            String feedCreateJson = objectMapper.writeValueAsString(feedCreate);

            // when
            mockMvc.perform(post("/feeds")
                            .header("authorization", "sol")
                            .contentType(APPLICATION_JSON)
                            .content(feedCreateJson)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.httpStatus").value("400"))
                    .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                    .andExpect(jsonPath("$.validation.title").value("바보는 금칙어입니다"))
                    .andDo(print());
        }
    }

    @Nested
    class 피드_조회 {
        @Test
        @DisplayName("피드 1개조회 성공")
        void 피드_1개조회_성공() throws Exception {
            // given
            var user = User.builder()
                    .email("sol@gmail.com")
                    .name("sol")
                    .password("1234")
                    .build();
            userRepository.save(user);
            var feed = Feed.builder()
                    .title("제목")
                    .content("내용")
                    .user(user)
                    .build();
            feedRepository.save(feed);

            // expected
            mockMvc.perform(get("/feeds/{feedId}", feed.getId())
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(feed.getId()))
                    .andExpect(jsonPath("$.title").value("제목"))
                    .andExpect(jsonPath("$.content").value("내용"))
                    .andDo(print());
        }

        @Test
        @DisplayName("피드 1개조회 실패 존재X")
        void 피드_1개조회_실패_존재X() throws Exception {
            // expected
            mockMvc.perform(get("/feeds/{feedId}", 1L)
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }


        @Test
        @DisplayName("피드 첫페이지 조회 성공")
        void 피드_첫페이지조회_성공() throws Exception {
            // given
            List<Feed> requestFeedList = IntStream.range(1, 31)
                    .mapToObj(i -> Feed.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .build())
                    .collect(Collectors.toList());
            feedRepository.saveAll(requestFeedList);

            // expected
            mockMvc.perform(get("/feeds?page=1&size=5&sort=id,desc")
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", Matchers.is(5)))
                    .andExpect(jsonPath("$[0].title").value("제목30"))
                    .andExpect(jsonPath("$[0].content").value("내용30"))
                    .andExpect(jsonPath("$[1].title").value("제목29"))
                    .andExpect(jsonPath("$[1].content").value("내용29"))
                    .andExpect(jsonPath("$[2].title").value("제목28"))
                    .andExpect(jsonPath("$[2].content").value("내용28"))
                    .andExpect(jsonPath("$[3].title").value("제목27"))
                    .andExpect(jsonPath("$[3].content").value("내용27"))
                    .andExpect(jsonPath("$[4].title").value("제목26"))
                    .andExpect(jsonPath("$[4].content").value("내용26"))
                    .andDo(print());
        }
    }

    @Nested
    class 피드_수정 {

        @Test
        @CustomWithMockUser(role = "ROLE_ADMIN")
        @DisplayName("피드 수정 성공")
        void 피드_수정_성공() throws Exception {
            var user = userRepository.findAll().get(0); // CustomWithMockUser 에서 만든 User를 가져옴
            var feed = Feed.builder()
                    .title("제목")
                    .content("내용")
                    .user(user)
                    .build();
            feedRepository.save(feed);
            var feedEdit = FeedEdit.builder()
                    .title("edit")
                    .content("edit content")
                    .build();

            // expected
            mockMvc.perform(patch("/feeds/{feedId}", feed.getId())
                            .header("authorization", "sol")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(feedEdit))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }
        @Test
        @CustomWithMockUser(role = "ROLE_ADMIN")
        @DisplayName("피드 수정 실패 존재X")
        void 피드_수정_실패_존재X() throws Exception {
            FeedEdit feedEdit = FeedEdit.builder().title("edit").content("edit content").build();

            // expected
            mockMvc.perform(patch("/feeds/{feedId}", 1)
                            .header("authorization", "sol")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(feedEdit))
                    )
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

    }

    @Nested
    class 피드_삭제 {

        @Test
        @CustomWithMockUser(role = "ROLE_USER")
        @DisplayName("피드 삭제 성공")
        void 피드_삭제_성공() throws Exception {
            // given
            var user = userRepository.findAll().get(0); // CustomWithMockUser 에서 만든 User를 가져옴
            var feed = Feed.builder()
                    .title("제목")
                    .content("내용")
                    .user(user)
                    .build();
            feedRepository.save(feed);

            // expected
            mockMvc.perform(delete("/feeds/{feedId}", feed.getId())
                            .header("authorization", "sol")
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }
}