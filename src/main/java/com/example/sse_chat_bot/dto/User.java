package com.example.sse_chat_bot.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class User {
    private String id; // UUID
    private String name;
    private List<String> chatHistory = new ArrayList<>();
}
