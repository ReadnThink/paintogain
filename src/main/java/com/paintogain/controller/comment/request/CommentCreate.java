package com.paintogain.controller.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentCreate {

    @Length(min = 1, max = 10, message = "작성자는 1~10자로 입력해주세요")
    @NotBlank(message = "작성자를 입력해주세요")
    private String author;

    @Length(min = 1, max = 1000, message = "내용은 1~1000자로 입력해주세요")
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder
    public CommentCreate(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
