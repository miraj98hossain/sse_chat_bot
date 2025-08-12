package com.example.sse_chat_bot.service;

import com.example.sse_chat_bot.entity.AppUserDetails;
import com.example.sse_chat_bot.entity.User;
import com.example.sse_chat_bot.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByPhone(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new AppUserDetails(user);
    }
}
