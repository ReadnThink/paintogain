package com.paintogain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.domain.Feed;
import com.paintogain.repository.FeedRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
    }

    @Nested
    class 피드_저장 {
        @Test
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
        void 피드_1개조회_성공() throws Exception {
            // given
            Feed feed = Feed.builder()
                    .title("title")
                    .content("content")
                    .build();
            feedRepository.save(feed);

            // expected
            mockMvc.perform(get("/feeds/{feedId}", feed.getId())
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(feed.getId()))
                    .andExpect(jsonPath("$.title").value("title"))
                    .andExpect(jsonPath("$.content").value("content"))
                    .andDo(print());
        }

        @Test
        void 피드_1개조회_실패_존재X() throws Exception {
            // expected
            mockMvc.perform(get("/feeds/{feedId}", 1L)
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }


        @Test
        void 피드_첫페이지조회_성공() throws Exception {
            // given
            List<Feed> requestFeedList = IntStream.range(1, 31)
                    .mapToObj(i -> Feed.builder()
                            .title("Title" + i)
                            .content("Content" + i)
                            .build())
                    .collect(Collectors.toList());
            feedRepository.saveAll(requestFeedList);

            // expected
            mockMvc.perform(get("/feeds?page=1&size=5&sort=id,desc")
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", Matchers.is(5)))
                    .andExpect(jsonPath("$[0].title").value("Title30"))
                    .andExpect(jsonPath("$[0].content").value("Content30"))
                    .andExpect(jsonPath("$[1].title").value("Title29"))
                    .andExpect(jsonPath("$[1].content").value("Content29"))
                    .andExpect(jsonPath("$[2].title").value("Title28"))
                    .andExpect(jsonPath("$[2].content").value("Content28"))
                    .andExpect(jsonPath("$[3].title").value("Title27"))
                    .andExpect(jsonPath("$[3].content").value("Content27"))
                    .andExpect(jsonPath("$[4].title").value("Title26"))
                    .andExpect(jsonPath("$[4].content").value("Content26"))
                    .andDo(print());
        }
    }

    @Nested
    class 피드_수정 {

        @Test
        void 피드_수정_성공() throws Exception {
            Feed feed = Feed.builder().title("title").content("content").build();
            FeedEdit feedEdit = FeedEdit.builder().title("edit").content("edit content").build();
            feedRepository.save(feed);

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
        void 피드_삭제_성공() throws Exception {
            Feed feed = Feed.builder().title("title").content("content").build();
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