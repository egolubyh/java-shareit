package ru.practicum.shareit.user.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
    }

    @Test
    void addNewUser() {
        when(userService.addNewUser(any(UserDto.class)))
                .thenReturn(userDto);

        UserDto actual = userController.addNewUser(userDto);

        assertEquals(userDto, actual);
    }

    @SneakyThrows
    @Test
    void findUser() {
        when(userService.findUserById(anyLong()))
                .thenReturn(userDto);

        UserDto actual = userController.findUser(1L);

        assertEquals(userDto, actual);
    }

    @Test
    void findAllUsers() {
        List<UserDto> expectedList = List.of(userDto);

        when(userService.findAllUsers()).thenReturn(expectedList);

        List<UserDto> actual = userController.findAllUsers();

        assertEquals(expectedList, actual);

    }

    @SneakyThrows
    @Test
    void updateUser() {
        when(userService.updateUserById(anyLong(),any(UserDto.class)))
                .thenReturn(userDto);

        UserDto actual = userController.updateUser(1L, userDto);

        assertEquals(userDto, actual);
    }

    @Test
    void deleteUser() {
        Long userId = 0L;
        userController.deleteUser(userId);

        verify(userService).deleteUserById(userId);
    }
}