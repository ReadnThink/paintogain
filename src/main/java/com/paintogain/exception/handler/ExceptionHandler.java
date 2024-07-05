package com.paintogain.exception.handler;

import com.paintogain.exception.custom.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus("400")
                .message("잘못된 요청입니다")
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> feedNotFound(GlobalException e) {
        int statusCode = e.getStatusCode();
        ErrorResponse body = ErrorResponse.builder()
                .httpStatus(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity
                .status(statusCode)
                .body(body);

        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("예외발생", e);

        ErrorResponse body = ErrorResponse.builder()
                .httpStatus("500")
                .message(e.getMessage())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity
                .status(500)
                .body(body);

        return response;
    }
}
