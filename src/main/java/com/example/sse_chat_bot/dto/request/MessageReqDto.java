package com.example.sse_chat_bot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageReqDto {
    private String content;
    private String senderType;
    private Long conversationId;
}
