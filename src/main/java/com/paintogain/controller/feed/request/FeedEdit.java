package com.paintogain.controller.feed.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FeedEdit {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder
    public FeedEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
