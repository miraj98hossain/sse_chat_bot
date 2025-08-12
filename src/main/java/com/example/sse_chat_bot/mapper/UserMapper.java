package com.example.sse_chat_bot.mapper;

import com.example.sse_chat_bot.dto.response.UserResDto;
import com.example.sse_chat_bot.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResDto userToUserResDto(User user);

    User userResDtoToUser(UserResDto userResDto);
}
