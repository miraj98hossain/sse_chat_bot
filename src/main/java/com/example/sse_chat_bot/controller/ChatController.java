package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.dto.request.MessageReqDto;
import com.example.sse_chat_bot.dto.response.MessageResDto;
import com.example.sse_chat_bot.entity.AppUserDetails;
import com.example.sse_chat_bot.mapper.SenderTypeMapper;
import com.example.sse_chat_bot.service.ConversationService;
import com.example.sse_chat_bot.service.MessageService;
import com.example.sse_chat_bot.utils.LoginCache;
import com.example.sse_chat_bot.utils.SenderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
        // Save user message
        var userMessage = messageService.createMessage(message);
        // Simulate server response
        String serverResponse = "Server: Hello " + loggedInUser.getUsername() + ", you said: " + message.getContent();
        MessageReqDto serverMessage = new MessageReqDto();
        serverMessage.setConversationId(message.getConversationId());
        serverMessage.setContent(serverResponse);
        serverMessage.setSenderType(SenderTypeMapper.mapToString(SenderType.SERVER));
        var serverRes = messageService.createMessage(serverMessage);

        sendToUser(loggedInUser.getUsername(), userMessage);
        sendToUser(loggedInUser.getUsername(), serverRes);

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
