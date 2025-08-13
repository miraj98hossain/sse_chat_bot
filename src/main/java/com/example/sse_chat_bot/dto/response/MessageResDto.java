package com.example.sse_chat_bot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResDto {
    private Long id;
    private String Content;
    private String senderType;
    private Long conversationId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
