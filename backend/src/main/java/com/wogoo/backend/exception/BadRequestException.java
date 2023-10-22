package com.wogoo.backend.exception;

public class BadRequestException extends Exception {
    private final String errorCode;

    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
