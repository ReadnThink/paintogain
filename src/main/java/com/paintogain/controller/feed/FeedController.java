package com.paintogain.controller.feed;

import com.paintogain.config.security.UserPrincipal;
import com.paintogain.controller.feed.request.FeedCreate;
import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import com.paintogain.service.feed.FeedService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/feeds")
    public void feed(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid FeedCreate feedCreate) {
        feedCreate.isContainBadWords();
        feedService.save(userPrincipal.getUserId(), feedCreate);
    }

    @GetMapping("/feeds/{feedId}")
    public FeedResponse get(@PathVariable Long feedId) {
        return feedService.get(feedId);
    }

    @GetMapping("/feeds")
    public List<FeedResponse> feeds(Pageable pageable) {
        return feedService.getFeedList(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping("/feeds/{feedId}")
    public void edit(@PathVariable Long feedId, @RequestBody @Valid FeedEdit feedEdit) {
        feedService.edit(feedId, feedEdit);
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_USER') && hasPermission(#feedId, 'POST', 'DELETE')")
    @DeleteMapping("/feeds/{feedId}")
    public void delete(@PathVariable Long feedId) {
        feedService.delete(feedId);
    }
}
