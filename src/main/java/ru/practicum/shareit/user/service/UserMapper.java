package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
@Service
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public void updateUser(UserDto userDto, User user) {
        String nameUserDto = userDto.getName();
        String emailUserDto = userDto.getEmail();

        if (nameUserDto != null) user.setName(nameUserDto);
        if (emailUserDto != null) user.setEmail(emailUserDto);
    }
}
