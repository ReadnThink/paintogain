package com.paintogain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        indexes = {
                @Index(name = "IDX_COMMNET_FEED_ID", columnList = "feed_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn
    private Feed feed;

    @Builder
    public Comment(Long id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public void addFeed(Feed feed) {
        this.feed = feed;
    }
}
