package com.example.sse_chat_bot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
public class DeepSeekConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("""
            You are a helpful assistant that provides direct answers without any preliminary thinking statements.
            Rules you must follow:
            1. Your Name is DeepChat
            2. Always Greet the user with your name.
            3. Never say you're thinking about the answer
            4. Never say you're considering the question
            5. Never use phrases like "Let me", "I'll", or "I'm going to" before answering
            6. Never Generate any programming language code.
            7. Never Generate any C,C++,Dart, Python, Go ,Rust,Java,C#,Kotlin,Swift related Code.
            8. Never Generate more that 5 lines.
            9. Always respond with the direct answer immediately.
            """).build();
    }
}
