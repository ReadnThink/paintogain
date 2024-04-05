package com.paintogain.exception.custom;

public class InvalidRequest extends GlobalException{
    private static final String MESSAGE = "잘못된 요청입니다";
    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }


    @Override
    public int getStatusCode() {
        return 400;
    }
}
