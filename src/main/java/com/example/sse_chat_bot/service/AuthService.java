package com.example.sse_chat_bot.service;

import com.example.sse_chat_bot.dto.request.LoginReqDto;
import com.example.sse_chat_bot.dto.request.UserReqDto;
import com.example.sse_chat_bot.dto.response.UserResDto;
import com.example.sse_chat_bot.entity.User;
import com.example.sse_chat_bot.mapper.UserMapper;
import com.example.sse_chat_bot.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private UserRepo userRepo;
    @Autowired(required = true)
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authManager;

    public void register(UserReqDto userReqDto) {
        User newUser = new User();
        newUser.setFirstName(userReqDto.getFirstName());
        newUser.setLastName(userReqDto.getLastName());
        newUser.setPassword(encoder.encode(userReqDto.getPassword()));
        newUser.setPhone(userReqDto.getPhone());
        userRepo.save(newUser);
    }

    public UserResDto login(LoginReqDto loginReqDto) {
        var user = userRepo.findByPhone(loginReqDto.getPhone()).orElseThrow(
                () -> new EntityNotFoundException("User not found with phone: " + loginReqDto.getPhone())
        );
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginReqDto.getPhone(),
                loginReqDto.getPassword());
        authManager.authenticate(authToken);
        return userMapper.userToUserResDto(user);
    }
}
