package com.example.sse_chat_bot.service;

import com.example.sse_chat_bot.dto.response.ConversationResDto;
import com.example.sse_chat_bot.entity.Conversation;
import com.example.sse_chat_bot.mapper.ConversationMapper;
import com.example.sse_chat_bot.repository.ConversationRepo;
import com.example.sse_chat_bot.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    @Autowired
    ConversationRepo conversationRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ConversationMapper conversationMapper;

    @Transactional
    public ConversationResDto createConversation(Long loggedUser) {
        var user = userRepo.findById(loggedUser).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + loggedUser)
        );
        int totalUserConv = user.getConversations().size();
        Conversation conversation = new Conversation();
        conversation.setCreatedBy(user);
        conversation.setTitle("New Conversation" + (totalUserConv + 1));
        return conversationMapper.conversationToConversationResDto(conversationRepo.save(conversation));
    }

    @Transactional
    public List<ConversationResDto> getConversations(Long loggedUser) {
        var user = userRepo.findById(loggedUser).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + loggedUser)
        );
        return user.getConversations().stream().map(conversationMapper::conversationToConversationResDto).collect(Collectors.toList());
    }
}
