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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/deep-chat/chat")
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
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(loggedInUser.getUsername(), emitter);
        List<MessageResDto> prevConv = new ArrayList<>();
        // Send previous history on connect
        if (conversationId == null) {
            var conv = conversationService.createConversation(loggedInUser.getUser().getId());
            conversationId = conv.getId();
        } else {
            prevConv = messageService.getMessages(conversationId);
        }
        prevConv
                .forEach(msg -> sendToUser(loggedInUser.getUsername(), msg));

        emitter.onCompletion(() -> emitters.remove(loggedInUser.getUsername()));
        emitter.onTimeout(() -> emitters.remove(loggedInUser.getUsername()));

        return emitter;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageReqDto message) {
        AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();

        // Save user's original message
        var userMessage = messageService.createMessage(message);
        sendToUser(loggedInUser.getUsername(), userMessage);

        // Call AI service (streaming)
        Flux<String> serverResponseFlux = deepSeekService.getAIResponse(message.getContent());

        // Process the stream
        serverResponseFlux.subscribe(chunk -> {
                    // For each new chunk from the AI
                    MessageReqDto serverMessage = new MessageReqDto();
                    serverMessage.setConversationId(message.getConversationId());
                    serverMessage.setContent(chunk); // this is just the current piece
                    serverMessage.setSenderType(SenderTypeMapper.mapToString(SenderType.SERVER));
                    var serverRes = messageService.createMessage(serverMessage);
                    sendToUser(loggedInUser.getUsername(), serverRes); // push to frontend
                },
                error -> {
                    MessageReqDto serverMessage = new MessageReqDto();
                    serverMessage.setConversationId(message.getConversationId());
                    serverMessage.setContent(error.getMessage());
                    serverMessage.setSenderType(SenderTypeMapper.mapToString(SenderType.SERVER));
                    var serverRes = messageService.createMessage(serverMessage);
                    sendToUser(loggedInUser.getUsername(), serverRes);
                },
                () -> {
                    // When the AI finishes streaming
                    System.out.println("AI stream completed");
                });
    }


    private void sendToUser(String userId, MessageResDto message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("chat").data(message));
            } catch (Exception e) {
                emitters.remove(userId);
            }
        }
    }
}
