package com.paintogain.exception.custom;

public class AlreadyExistsEmailException extends GlobalException{

    public static String MESSAGE = "이미 존재하는 이메일입니다";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
