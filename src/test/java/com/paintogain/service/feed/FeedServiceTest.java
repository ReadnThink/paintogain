package com.paintogain.service.feed;

import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import com.paintogain.domain.Feed;
import com.paintogain.exception.custom.FeedNotFound;
import com.paintogain.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FeedServiceTest {

    @Autowired
    private FeedService feedService;

    @Autowired
    private FeedRepository feedRepository;

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
    }

    @Nested
    class 피드_저장 {
        @Test
        void 피드작성() {
            // given
            FeedCreate feedCreate = FeedCreate.builder()
                    .title("title")
                    .content("Hello world")
                    .build();

            // when
            feedService.save(feedCreate);

            //then
            assertThat(feedRepository.count()).isEqualTo(1L);
            assertThat(feedRepository.findAll().get(0).getTitle()).isEqualTo("title");
        }
    }

    @Nested
    class 피드_조회 {

        @Test
        void 피드조회_성공() {
            // given
            Feed create = Feed.builder().title("title").content("content").build();
            Feed savedFeed = feedRepository.save(create);
            // when
            FeedResponse feedResponse = feedService.get(savedFeed.getId());

            // then
            assertThat(feedResponse.getId()).isEqualTo(savedFeed.getId());
            assertThat(feedResponse.getTitle()).isEqualTo("title");
            assertThat(feedResponse.getContent()).isEqualTo("content");
        }

        @Test
        void 피드조회_실패_존재X() {
            // given
            Long feedId = 1L;

            // expected
            assertThatThrownBy(() -> feedService.get(feedId))
                    .isInstanceOf(FeedNotFound.class);
        }

        @Test
        void 피드_첫페이지_조회() {
            // given
            List<Feed> requestFeedList = IntStream.range(1, 31)
                    .mapToObj(i -> Feed.builder()
                            .title("Title" + i)
                            .content("Content" + i)
                            .build())
                    .collect(Collectors.toList());
            feedRepository.saveAll(requestFeedList);

            // when
            Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
            List<FeedResponse> feedList = feedService.getFeedList(pageable);

            // then
            assertThat(feedList.size()).isEqualTo(5);
            assertThat("Title30").isEqualTo(feedList.get(0).getTitle());
            assertThat("Title29").isEqualTo(feedList.get(1).getTitle());
            assertThat("Title28").isEqualTo(feedList.get(2).getTitle());
            assertThat("Title27").isEqualTo(feedList.get(3).getTitle());
            assertThat("Title26").isEqualTo(feedList.get(4).getTitle());
        }
    }

    @Nested
    class 피드_수정 {

        @Test
        void 피드_제목_수정_성공() {
            // given
            Feed feed = Feed.builder().title("title").content("content").build();
            FeedEdit feedEdit = FeedEdit.builder().title("edit").content("edit content").build();
            feedRepository.save(feed);

            // when
            feedService.edit(feed.getId(), feedEdit);

            // then
            Feed changedFeed = feedRepository.findById(feed.getId())
                    .orElseThrow(() -> new FeedNotFound());
            assertThat(changedFeed.getTitle()).isEqualTo("edit");
            assertThat(changedFeed.getContent()).isEqualTo("edit content");

        }

        @Test
        void 피드_수정_실패_존재X() {
            // given
            Feed feed = Feed.builder().title("title").content("content").build();
            FeedEdit feedEdit = FeedEdit.builder().title("edit").content("edit content").build();
            feedRepository.save(feed);

            // expected
            assertThatThrownBy(() -> feedService.edit(feed.getId()+1, feedEdit))
                    .isInstanceOf(FeedNotFound.class);
        }
    }

    @Nested
    class 피드_삭제 {

        @Test
        void 피드_삭제_성공() {
            // given
            Feed feed = Feed.builder().title("title").content("content").build();
            FeedEdit feedEdit = FeedEdit.builder().title("edit").content("edit content").build();
            feedRepository.save(feed);

            // when
            feedService.delete(feed.getId());

            // then
            assertThat(feedRepository.count()).isEqualTo(0);
        }

        @Test
        void 피드_삭제_실패_존재X() {
            // given
            Long feedId = 1L;

            // expected
            assertThatThrownBy(() -> feedService.delete(feedId))
                    .isInstanceOf(FeedNotFound.class);
        }
    }

}