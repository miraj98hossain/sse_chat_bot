package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.request.LoginReqDto;
import com.example.sse_chat_bot.dto.request.UserReqDto;
import com.example.sse_chat_bot.dto.response.UserResDto;
import com.example.sse_chat_bot.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/deep-chat/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserReqDto userReqDto) {
        try {
            authService.register(userReqDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred during Register", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResDto> login(@RequestBody LoginReqDto loginReqDto) {
        try {

            return ResponseEntity.ok().body(authService.login(loginReqDto));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error occurred during login", e);
        }
    }
}

