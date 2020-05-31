package com.course.work;

import com.course.work.entity.Users;
import com.course.work.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findUsersByUsername("admin").isEmpty()) {
            userRepository.save(new Users("admin", passwordEncoder.encode("admin"),
                    Collections.singletonList("ROLE_ADMIN")));
        }
        if (userRepository.findUsersByUsername("user").isEmpty()) {
            userRepository.save(new Users("user", passwordEncoder.encode("user"),
                    Collections.singletonList("ROLE_USER")));
        }

    }
}
