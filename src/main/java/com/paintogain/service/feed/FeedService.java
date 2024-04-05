package com.paintogain.service.feed;

import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import com.paintogain.domain.Feed;
import com.paintogain.exception.custom.FeedNotFound;
import com.paintogain.repository.FeedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FeedService {
    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public void save(FeedCreate feedCreate) {
        Feed feed = Feed.builder()
                .title(feedCreate.getTitle())
                .content(feedCreate.getContent())
                .build();
        feedRepository.save(feed);
    }

    public FeedResponse get(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFound());
        return feed.toFeedResponse();
    }

    public List<FeedResponse> getFeedList(Pageable pageable) {
        return feedRepository.findAll(pageable)
                .stream()
                .map(Feed::toFeedResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long feedId, FeedEdit feedEdit) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFound());

        feed.edit(feedEdit);
    }

    @Transactional
    public void delete(Long feedId) {
        feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFound());

        feedRepository.deleteById(feedId);
    }
}