package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.response.ConversationResDto;
import com.example.sse_chat_bot.entity.AppUserDetails;
import com.example.sse_chat_bot.service.ConversationService;
import com.example.sse_chat_bot.utils.LoginCache;
import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deep-chat/conv")
public class ConversationController {
    @Autowired
    private ConversationService conversationService;
    @GetMapping("/get-conversations")
    public ResponseEntity<List<ConversationResDto>> getUserConversation() {
        try {
            AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();
            var conversations= conversationService.getConversations(loggedInUser.getUser().getId());
            if(conversations.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
