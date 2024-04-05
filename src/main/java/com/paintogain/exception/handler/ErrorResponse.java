package com.paintogain.exception.handler;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *      "code" : "400",
 *      "message" : "잘못된 요청입니다.",
 *      "validation" : {
 *          "title": "제목을 입력해주세요"
 *          "content": "내용을 입력해주세요"
 *      }
 * }
 */
@Getter
public class ErrorResponse {

    private final String httpStatus;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String httpStatus, String message, Map<String, String> validation) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
