package com.example.sse_chat_bot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
}
