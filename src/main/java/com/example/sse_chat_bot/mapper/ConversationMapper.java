package com.example.sse_chat_bot.mapper;

import com.example.sse_chat_bot.dto.response.ConversationResDto;
import com.example.sse_chat_bot.entity.Conversation;
import com.example.sse_chat_bot.entity.User;
import com.example.sse_chat_bot.repository.UserRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ConversationMapper {

    @Autowired
    protected UserRepo userRepository;

    @Mapping(source = "createdBy.id", target = "createdBy")
    public abstract ConversationResDto conversationToConversationResDto(Conversation conversation);

    @Mapping(source = "createdBy", target = "createdBy")
    public abstract Conversation conversationResDtoToConversation(ConversationResDto conversationResDto);

    // Map Long → User (load from DB)
    protected User map(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    // Map User → Long
    protected Long map(User user) {
        return user != null ? user.getId() : null;
    }
}
