package com.example.sse_chat_bot.mapper;

import com.example.sse_chat_bot.dto.response.MessageResDto;
import com.example.sse_chat_bot.entity.Conversation;
import com.example.sse_chat_bot.entity.Message;
import com.example.sse_chat_bot.repository.ConversationRepo;
import com.example.sse_chat_bot.utils.SenderType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    @Autowired
    private ConversationRepo conversationRepo;

    @Mapping(source = "senderType", target = "senderType", qualifiedByName = "mapToString")
    @Mapping(source = "conversation.id", target = "conversationId")
    public abstract MessageResDto messageToMessageResDto(Message message);

    @Mapping(source = "senderType", target = "senderType", qualifiedByName = "mapToType")
    @Mapping(source = "conversationId", target = "conversation")
    public abstract Message messageDtoToMessage(MessageResDto messageResDto);

    @Named("mapToString")
    protected String mapToString(SenderType senderType) {
        return SenderTypeMapper.mapToString(senderType);
    }

    @Named("mapToType")
    protected SenderType mapToType(String senderType) {
        return SenderTypeMapper.mapToSenderType(senderType);
    }

    // Map Long → Conversation (load from DB)
    protected Conversation map(Long id) {
        if (id == null) return null;
        return conversationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found with id: " + id));
    }

    // Map Conversation → Long
    protected Long map(Conversation conversation) {
        return conversation != null ? conversation.getId() : null;
    }
}
