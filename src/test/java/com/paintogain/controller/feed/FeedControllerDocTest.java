package com.paintogain.controller.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.domain.Feed;
import com.paintogain.repository.FeedRepository;
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
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @Test
    @DisplayName("피드 단건조회 테스트")
    void 피드_조회() throws Exception {
        // given
        Feed feed = Feed.builder()
                .title("제목")
                .content("내용")
                .build();
        feedRepository.save(feed);

        mockMvc.perform(get("/feeds/{feedId}", 1L)
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
    @DisplayName("피드 저장 테스트")
    void 피드_저장() throws Exception {
        // given
        FeedCreate feedCreate = FeedCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        String feedCreateJson = objectMapper.writeValueAsString(feedCreate);

        mockMvc.perform(post("/feeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(feedCreateJson)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("feed-create",
                            requestFields(
                                    fieldWithPath("title").description("제목")
                                            .attributes(key("constraint").value("제목을 입력해 주세요")),
                                    fieldWithPath("content").description("내용").optional()
                            )
                        )
                );
    }
}
