package com.course.work.repository;

import com.course.work.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByUsername(String username);
    Optional<Users> findUsersByUsernameAndPass(String username, String pass);
}
