package com.course.work.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFounException extends RuntimeException {
    public UserNotFounException(String name) {
        super("No user with username "+ name);
    }
}