package com.course.work.exception;

public class InvalidJwtAuthenticationException extends RuntimeException {
    public InvalidJwtAuthenticationException(String expired_or_invalid_token) {
        super(expired_or_invalid_token);
    }
}
