//package com.example.sse_chat_bot.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@RestController
//@RequestMapping("/chat")
//public class ChatController {
//
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final Map<String, List<String>> chatHistories = new ConcurrentHashMap<>();
//
//    @GetMapping("/stream/{userId}")
//    public SseEmitter stream(@PathVariable String userId) {
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//        emitters.put(userId, emitter);
//
//        // Send previous history on connect
//        chatHistories.getOrDefault(userId, new ArrayList<>())
//                .forEach(msg -> sendToUser(userId, msg));
//
//        emitter.onCompletion(() -> emitters.remove(userId));
//        emitter.onTimeout(() -> emitters.remove(userId));
//
//        return emitter;
//    }
//
//    @PostMapping("/send/{userId}")
//    public void sendMessage(@PathVariable String userId, @RequestParam String message) {
//        // Save user message
//        chatHistories.computeIfAbsent(userId, k -> new ArrayList<>()).add("User: " + message);
//
//        // Simulate server response
//        String serverResponse = "Server: Hello "+ userId + ", you said: " + message;
//        chatHistories.get(userId).add(serverResponse);
//
//        sendToUser(userId, "User: " + message);
//        sendToUser(userId, serverResponse);
//
//    }
//
//    private void sendToUser(String userId, String message) {
//        SseEmitter emitter = emitters.get(userId);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event().name("chat").data(message));
//            } catch (Exception e) {
//                emitters.remove(userId);
//            }
//        }
//    }
//}
