package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.response.MessageResDto;
import com.example.sse_chat_bot.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deep-chat/msg")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @GetMapping("/get-messages")
    public ResponseEntity<List<MessageResDto>> getAllMessages(@RequestParam Long conversationId) {
        try{
          var response=  messageService.getMessages(conversationId);
            if(response.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
