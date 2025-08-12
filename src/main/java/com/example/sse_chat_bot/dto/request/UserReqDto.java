package com.example.sse_chat_bot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDto {
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
}
