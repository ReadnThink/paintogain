package com.paintogain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.annotation.CustomWithMockUser;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.domain.Feed;
import com.paintogain.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.paintogin.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class FeedControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    FeedRepository feedRepository;

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
    }

    @Test
    @DisplayName("피드 단건조회 테스트")
    void 피드_단건조회() throws Exception {
        // given
        Feed feed = Feed.builder()
                .title("제목")
                .content("내용")
                .build();
        feedRepository.save(feed);

        // expect
        mockMvc.perform(get("/feeds/{feedId}", feed.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-inquiry", pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("피드 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("피드 목록 조회 테스트 - 10건씩 페이징 처리")
    void 피드_목록조회() throws Exception {
        // given
        List<Feed> requestFeedList = IntStream.range(1, 31)
                .mapToObj(i -> Feed.builder()
                        .title("Title" + i)
                        .content("Content" + i)
                        .build())
                .collect(Collectors.toList());
        feedRepository.saveAll(requestFeedList);

        // expect
        mockMvc.perform(get("/feeds?page=1&size=10&sort=id,desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-list",queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬정보")
                        ),

                        responseFields(
                                fieldWithPath("[].id").description("피드 ID"),
                                fieldWithPath("[].title").description("제목"),
                                fieldWithPath("[].content").description("내용")
                        )
                ));
    }

    @Test
    @CustomWithMockUser()
    @DisplayName("피드 저장 테스트")
    void 피드_저장() throws Exception {
        // given
        FeedCreate feedCreate = FeedCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(feedCreate);

        // expect
        mockMvc.perform(post("/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-create",
                                requestFields(
                                        fieldWithPath("title").description("제목")
                                                .attributes(key("constraint").value("제목을 입력해 주세요"))
                                                .optional(),
                                        fieldWithPath("content").description("내용")
                                                .attributes(key("constraint").value("내용을 입력해 주세요"))
                                                .optional()
                                )
                        )
                );
    }

    @Test
    @CustomWithMockUser(role = "ROLE_ADMIN")
    @DisplayName("피드 수정 테스트")
    void 피드_수정() throws Exception {
        Feed feed = Feed.builder()
                .title("제목")
                .content("내용")
                .build();
        feedRepository.save(feed);

        // given
        FeedEdit feedEdit = FeedEdit.builder()
                .title("제목")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(feedEdit);

        // expect
        mockMvc.perform(patch("/feeds/{feedId}", feed.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-edit", pathParameters(
                                        parameterWithName("feedId").description("피드 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("제목")
                                                .attributes(key("constraint").value("수정할 제목을 입력해 주세요"))
                                                .optional(),
                                        fieldWithPath("content").description("내용")
                                                .attributes(key("constraint").value("수정할 내용을 입력해 주세요"))
                                                .optional()
                                )
                        )
                );
    }

    @Test
    @CustomWithMockUser(role = "ROLE_ADMIN")
    @DisplayName("피드 단건삭제 테스트")
    void 피드_삭제() throws Exception {
        // given
        Feed feed = Feed.builder()
                .title("제목")
                .content("내용")
                .build();
        feedRepository.save(feed);

        // expect
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/feeds/{feedId}", feed.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-delete", pathParameters(
                                parameterWithName("feedId").description("피드 ID")
                        )
                ));
    }
}
