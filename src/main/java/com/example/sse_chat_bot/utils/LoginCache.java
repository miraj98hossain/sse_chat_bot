package com.example.sse_chat_bot.utils;

import com.example.sse_chat_bot.entity.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class LoginCache {
    public static LoginCache instance;

    private LoginCache() {

    }

    public static LoginCache getInstance() {
        if (Objects.isNull(instance)) {
            instance = new LoginCache();
        }

        return instance;
    }

    public AppUserDetails getCurrentLoggenInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof AppUserDetails) {
            return (AppUserDetails) authentication.getPrincipal();
        }

        return null;
    }
}
