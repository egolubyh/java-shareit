package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

    UserDto findUserById(Long userId) throws UserNotFoundException;

    List<UserDto> findAllUsers();

    UserDto updateUserById(Long userId, UserDto userDto) throws UserNotFoundException;

    void deleteUserById(Long userId);
}