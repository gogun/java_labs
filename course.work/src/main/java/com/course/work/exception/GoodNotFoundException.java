package com.course.work.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GoodNotFoundException extends RuntimeException{
    public GoodNotFoundException(Long id) {
        super("Couldn't find good with id " + id);
    }

    public GoodNotFoundException(String name) {
        super("Couldn't find good with name " + name);
    }
}
