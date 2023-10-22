package com.wogoo.backend.exception;

import lombok.Data;

@Data
public class NotFoundException extends Exception {
    private final ErrorInfo errorInfo;

    public NotFoundException(String message, String errorCode) {
        super(message);
        this.errorInfo = new ErrorInfo(message, errorCode);
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public String getErrorCode() {
        return errorInfo.errorCode();
    }

    public static record ErrorInfo(String message, String errorCode) {
    }
}
