package com.example.sse_chat_bot.repository;

import com.example.sse_chat_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
}
