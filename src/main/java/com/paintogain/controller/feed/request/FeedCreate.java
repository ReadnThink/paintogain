package com.paintogain.controller.feed.request;

import com.paintogain.exception.custom.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FeedCreate {
    @NotBlank(message = "제목을 입력해주세요")
    private final String title;
    @NotBlank(message = "내용을 입력해주세요")
    private final String content;

    @Builder
    public FeedCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void isContainBadWords() {
        if (this.title.contains("바보")) throw new InvalidRequest("title", "바보는 금칙어입니다");
        if (this.content.contains("바보")) throw new InvalidRequest("content", "바보는 금칙어입니다");
    }
}
