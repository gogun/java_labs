package com.course.work.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException(Long id) {
        super("Couldn't find sale with id " + id);
    }
}
