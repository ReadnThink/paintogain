package com.paintogain.domain;

import com.paintogain.controller.feed.request.FeedEdit;
import com.paintogain.controller.feed.response.FeedResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Feed(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public FeedResponse toFeedResponse() {
        return FeedResponse.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }

    public void edit(FeedEdit feedEdit) {
        this.title = feedEdit.getTitle();
        this.content = feedEdit.getContent();
    }
}
