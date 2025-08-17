package com.example.sse_chat_bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DeepSeekService {
    @Autowired
    private  ChatClient chatClient;
    public Flux<String> getAIResponse(String message) {
        try {

            return chatClient.prompt(message)
                    .stream()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching AI response, ex = " + e.getMessage());
        }
    }
}
