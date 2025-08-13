package com.example.sse_chat_bot.repository;

import com.example.sse_chat_bot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
