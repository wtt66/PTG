package com.example.reigie.common;

public class NotAuthenticationException extends RuntimeException{

    public NotAuthenticationException() {
    }

    public NotAuthenticationException(String message) {
        super(message);
    }

    public NotAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthenticationException(Throwable cause) {
        super(cause);
    }

    public NotAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
