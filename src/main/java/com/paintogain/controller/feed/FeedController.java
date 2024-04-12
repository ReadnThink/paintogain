package com.paintogain.controller.feed;

import com.paintogain.config.data.UserSession;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import com.paintogain.service.feed.FeedService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/test")
    public Long test(UserSession userSession) {
        log.info("UserSession >>> {}", userSession.id);
        return userSession.getId();
    }

    @PostMapping("/feeds")
    public void feed(@RequestBody @Valid FeedCreate feedCreate, @RequestHeader String authorization) {
        if (authorization.equals("sol")) {
            feedCreate.isContainBadWords();
            feedService.save(feedCreate);
        }
    }

    @GetMapping("/feeds/{feedId}")
    public FeedResponse get(@PathVariable Long feedId) {
        return feedService.get(feedId);
    }

    @GetMapping("/feeds")
    public List<FeedResponse> feeds(Pageable pageable) {
        return feedService.getFeedList(pageable);
    }

    @PatchMapping("/feeds/{feedId}")
    public void edit(@PathVariable Long feedId, @RequestBody @Valid FeedEdit feedEdit, @RequestHeader String authorization) {
        if (authorization.equals("sol")) {
            feedService.edit(feedId, feedEdit);
        }
    }

    @DeleteMapping("/feeds/{feedId}")
    public void delete(@PathVariable Long feedId, @RequestHeader String authorization) {
        if (authorization.equals("sol")) {
            feedService.delete(feedId);
        }
    }
}
