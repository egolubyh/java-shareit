package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.InvalidEmailException;
import ru.practicum.shareit.user.exception.NoEmailException;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto) throws DuplicateEmailException, NoEmailException, InvalidEmailException;
    UserDto findUserById(Long userId);
    List<UserDto> findAllUsers();
    UserDto updateUserById(Long userId, UserDto userDto) throws DuplicateEmailException, InvalidEmailException;
    void deleteUserById(Long userId);
}
