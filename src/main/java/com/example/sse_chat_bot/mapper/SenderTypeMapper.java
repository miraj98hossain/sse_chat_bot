package com.example.sse_chat_bot.mapper;

import com.example.sse_chat_bot.utils.SenderType;

public class SenderTypeMapper {

    public static String mapToString(SenderType senderType) {
        return switch (senderType) {
            case SERVER -> "Server";
            case USER -> "User";
            default -> "Unknown";
        };
    }

    public static SenderType mapToSenderType(String senderType) {
        if (senderType == null || senderType.equals("Server"))
            return SenderType.SERVER;
        return SenderType.USER;
    }
}
