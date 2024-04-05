package com.paintogain.controller.feed.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedResponse {
    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public FeedResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
