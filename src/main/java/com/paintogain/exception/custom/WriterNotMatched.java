package com.paintogain.exception.custom;

public class WriterNotMatched extends GlobalException{
    private static final String MESSAGE = "작성자가 일치하지 않습니다";
    public WriterNotMatched() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
