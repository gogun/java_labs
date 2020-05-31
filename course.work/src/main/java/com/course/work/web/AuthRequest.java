package com.course.work.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthRequest implements Serializable {
    private String username;
    private String password;
}
