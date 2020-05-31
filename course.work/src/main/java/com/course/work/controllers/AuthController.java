package com.course.work.controllers;

import com.course.work.entity.Users;
import com.course.work.exception.UserNotFounException;
import com.course.work.repository.UserRepository;
import com.course.work.security.jwt.JwtTokenProvider;
import com.course.work.web.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthRequest authRequest) {
        try {
            String name = authRequest.getUsername();
            Users user = userRepository.findUsersByUsername(name)
                    .orElseThrow(() -> new UserNotFounException("User not found"));
            String token = "";
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                token = jwtTokenProvider.createToken(
                        name,
                        user.getRoles()
                );
            } else {
                throw new UserNotFounException(name);
            }

            Map<Object, Object> response = new HashMap<>();
            response.put("role", user.getRoles().get(0));
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid data");
        }
    }
}
