package com.example.sse_chat_bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DeepSeekService {
    private final ChatClient chatClient;
    public DeepSeekService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                You are an advanced AI assistant integrated into a Spring Boot application.
                - Respond **directly** and **concisely** without unnecessary reasoning steps.
                - Avoid phrases like "Let me think..." or step-by-step explanations unless explicitly asked.
                - Provide clear, short, and accurate answers.
                - Respond in English only, regardless of the input language.
                """)
                .build();
    }
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
