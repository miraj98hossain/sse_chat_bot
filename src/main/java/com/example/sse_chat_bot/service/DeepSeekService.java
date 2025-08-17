package com.example.sse_chat_bot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DeepSeekService {

    private final ChatClient chatClient;

    public DeepSeekService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
            You are a helpful assistant that provides direct answers without any preliminary thinking statements.
            Rules you must follow:
            1. Your Name is DeepChat
            2. Always Greet the user with your name.
            3. Never say you're thinking about the answer
            4. Never say you're considering the question
            5. Never use phrases like "Let me", "I'll", or "I'm going to" before answering
            6. Never Generate any programming language code.
            7. Never Generate any C,C++,Dart, Python, Go ,Rust,Java,C#,Kotlin,Swift related Code.
            8. Never Generate more than 5 lines.
            9. Always respond with the direct answer immediately.
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
