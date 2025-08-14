package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.request.MessageReqDto;
import com.example.sse_chat_bot.dto.response.MessageResDto;
import com.example.sse_chat_bot.entity.AppUserDetails;
import com.example.sse_chat_bot.mapper.SenderTypeMapper;
import com.example.sse_chat_bot.service.ConversationService;
import com.example.sse_chat_bot.service.DeepSeekService;
import com.example.sse_chat_bot.service.MessageService;
import com.example.sse_chat_bot.utils.LoginCache;
import com.example.sse_chat_bot.utils.SenderType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/deep-chat/chat")
@Slf4j
public class ChatController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    @Autowired
    private MessageService messageService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private DeepSeekService deepSeekService;

    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam(required = false) Long conversationId) {
        AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();
        String username = loggedInUser.getUsername();

        // Long timeout for SSE (could also be set to something like 30 mins)
        SseEmitter emitter = new SseEmitter(0L); // 0 means no timeout
        emitters.put(username, emitter);

        try {
            // Ensure conversation exists or create new one
            if (conversationId == null) {
                var conv = conversationService.createConversation(loggedInUser.getUser().getId());
                conversationId = conv.getId();

            } else {
                // Load previous conversation messages
                List<MessageResDto> prevMessages = messageService.getMessages(conversationId);
                for (MessageResDto msg : prevMessages) {
                    sendToUser(username, msg);
                }
            }
        } catch (EntityNotFoundException e) {
            log.warn("Conversation not found for user {} with id {}", username, conversationId, e);
        }

        // Handle cleanup on completion / timeout / error
        emitter.onCompletion(() -> {
            emitters.remove(username);
            log.debug("SSE completed for {}", username);
        });

        emitter.onTimeout(() -> {
            emitters.remove(username);
            log.debug("SSE timed out for {}", username);
        });

        emitter.onError((ex) -> {
            emitters.remove(username);
            log.error("SSE error for {}", username, ex);
        });

        return emitter;
    }


    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageReqDto message) {
        AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();
        message.setSenderType(SenderTypeMapper.mapToString(SenderType.USER));
        // Save user's original message
        var userMessage = messageService.createMessage(message);
        sendToUser(loggedInUser.getUsername(), userMessage);

        // Call AI service (streaming)
        Flux<String> serverResponseFlux = deepSeekService.getAIResponse(message.getContent());
        // StringBuilder to accumulate the chunks
        StringBuilder fullResponse = new StringBuilder();
        // Process the stream
        serverResponseFlux.subscribe(chunk -> {
                    fullResponse.append(chunk);
                    // For each new chunk from the AI
                    MessageResDto serverMessage = new MessageResDto();
                    serverMessage.setConversationId(message.getConversationId());
                    serverMessage.setContent(chunk); // this is just the current piece
                    serverMessage.setSenderType(SenderTypeMapper.mapToString(SenderType.SERVER));
                    serverMessage.setCreatedAt(OffsetDateTime.now());
                    sendToUser(loggedInUser.getUsername(), serverMessage); // push to frontend
                },
                error -> {

                    System.out.println("AI Error"+error.toString());
                },
                () -> {
                    MessageReqDto serverMessage = new MessageReqDto();
                    serverMessage.setConversationId(message.getConversationId());
                    serverMessage.setContent(fullResponse.toString());
                    serverMessage.setSenderType(SenderTypeMapper.mapToString(SenderType.SERVER));
                    var serverRes = messageService.createMessage(serverMessage);
                    // When the AI finishes streaming
                    System.out.println("AI stream completed");
                });
    }


    private void sendToUser(String userId, MessageResDto message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("deep-chat").data(message));
            } catch (Exception e) {
                emitters.remove(userId);
            }
        }
    }
}
