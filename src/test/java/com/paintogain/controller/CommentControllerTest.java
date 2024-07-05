package com.paintogain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.annotation.CustomWithMockUser;
import com.paintogain.controller.comment.request.CommentCreate;
import com.paintogain.domain.Comment;
import com.paintogain.domain.Feed;
import com.paintogain.domain.User;
import com.paintogain.repository.comment.CommentRepository;
import com.paintogain.repository.feed.FeedRepository;
import com.paintogain.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void setUp() {
        feedRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Nested
    class 피드_댓글_저장 {
        @Test
        @CustomWithMockUser
        @DisplayName("댓글 작성")
        void write() throws Exception {
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

            CommentCreate commentCreate = CommentCreate.builder()
                    .author("sol")
                    .content("댓글입니다")
                    .build();

            // expected
            mockMvc.perform(post("/feeds/{feedId}/comments", feed.getId())
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentCreate))
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class 피드_댓글_삭제 {

        @Test
        @CustomWithMockUser
        @DisplayName("댓글 삭제")
        void 피드_삭제_성공() throws Exception {
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

            var comment = Comment.builder()
                    .content("댓글")
                    .author(user.getEmail())
                    .build();
            comment.addFeed(feed);
            commentRepository.save(comment);

            // when
            mockMvc.perform(delete("/comments/{commentId}", comment.getId())
                            .header("authorization", "sol")
                    )
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}