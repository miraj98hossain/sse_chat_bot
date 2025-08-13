package com.example.sse_chat_bot.repository;

import com.example.sse_chat_bot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepo extends JpaRepository<Conversation, Long> {
}
