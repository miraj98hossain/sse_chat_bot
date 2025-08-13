package com.example.sse_chat_bot.service;

import com.example.sse_chat_bot.dto.request.MessageReqDto;
import com.example.sse_chat_bot.dto.response.MessageResDto;
import com.example.sse_chat_bot.entity.Message;
import com.example.sse_chat_bot.mapper.MessageMapper;
import com.example.sse_chat_bot.mapper.SenderTypeMapper;
import com.example.sse_chat_bot.repository.ConversationRepo;
import com.example.sse_chat_bot.repository.MessageRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private ConversationRepo conversationRepo;
    @Autowired
    private MessageMapper messageMapper;

    @Transactional
    public MessageResDto createMessage(MessageReqDto message) {
        var conversation = conversationRepo.findById(message.getConversationId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Conversation with id " + message.getConversationId() + " not found")
                );
        Message newMessage = new Message();
        newMessage.setConversation(conversation);
        newMessage.setContent(message.getContent());
        newMessage.setSenderType(SenderTypeMapper.mapToSenderType(message.getSenderType()));
        return messageMapper.messageToMessageResDto(messageRepo.save(newMessage));
    }

    @Transactional
    public List<MessageResDto> getMessages(Long conversationId) {
        var conversation = conversationRepo.findById(conversationId).orElseThrow(
                () -> new EntityNotFoundException("Conversation with id: " + conversationId)
        );
        return conversation.getMessages().stream().map(messageMapper::messageToMessageResDto).collect(Collectors.toList());
    }
}
