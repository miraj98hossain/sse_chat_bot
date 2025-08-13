package com.example.sse_chat_bot.controller;

import com.example.sse_chat_bot.entity.AppUserDetails;
import com.example.sse_chat_bot.utils.LoginCache;
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
    private final Map<String, List<String>> chatHistories = new ConcurrentHashMap<>();

    @GetMapping("/stream")
    public SseEmitter stream() {
        AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(loggedInUser.getUsername(), emitter);

        // Send previous history on connect
        chatHistories.getOrDefault(loggedInUser.getUsername(), new ArrayList<>())
                .forEach(msg -> sendToUser(loggedInUser.getUsername(), msg));

        emitter.onCompletion(() -> emitters.remove(loggedInUser.getUsername()));
        emitter.onTimeout(() -> emitters.remove(loggedInUser.getUsername()));

        return emitter;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam String message) {
        AppUserDetails loggedInUser = LoginCache.getInstance().getCurrentLoggenInUser();
        // Save user message
        chatHistories.computeIfAbsent(loggedInUser.getUsername(), k -> new ArrayList<>()).add("User: " + message);

        // Simulate server response
        String serverResponse = "Server: Hello " + loggedInUser.getUsername() + ", you said: " + message;
        chatHistories.get(loggedInUser.getUsername()).add(serverResponse);

        sendToUser(loggedInUser.getUsername(), "User: " + message);
        sendToUser(loggedInUser.getUsername(), serverResponse);

    }

    private void sendToUser(String userId, String message) {
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
