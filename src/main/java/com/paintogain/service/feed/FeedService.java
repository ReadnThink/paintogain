package com.paintogain.service.feed;

import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import com.paintogain.domain.Feed;
import com.paintogain.exception.custom.FeedNotFound;
import com.paintogain.exception.custom.UserNotFound;
import com.paintogain.repository.feed.FeedRepository;
import com.paintogain.repository.user.UserRepository;
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
    private final UserRepository userRepository;

    public FeedService(FeedRepository feedRepository, UserRepository userRepository) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
    }

    public void save(Long userId, FeedCreate feedCreate) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        Feed feed = Feed.builder()
                .user(user)
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