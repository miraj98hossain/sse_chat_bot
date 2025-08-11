package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    @PostMapping("/register")
    public User register(@RequestParam String name) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        userStore.put(user.getId(), user);
        return user;
    }
}

