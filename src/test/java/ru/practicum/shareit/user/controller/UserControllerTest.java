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
    private static final String FAIL_USER_MESSAGE = "Возвращаемый user не соответствует ожидаемому";
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
    void testAddNewUser() {
        when(userService.addNewUser(any(UserDto.class)))
                .thenReturn(userDto);

        UserDto actual = userController.addNewUser(userDto);

        assertEquals(userDto, actual, FAIL_USER_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindUser() {
        when(userService.findUserById(anyLong()))
                .thenReturn(userDto);

        UserDto actual = userController.findUser(1L);

        assertEquals(userDto, actual, FAIL_USER_MESSAGE);
    }

    @Test
    void testFindAllUsers() {
        List<UserDto> expectedList = List.of(userDto);

        when(userService.findAllUsers()).thenReturn(expectedList);

        List<UserDto> actual = userController.findAllUsers();

        assertEquals(expectedList, actual, FAIL_USER_MESSAGE);

    }

    @SneakyThrows
    @Test
    void testUpdateUser() {
        when(userService.updateUserById(anyLong(),any(UserDto.class)))
                .thenReturn(userDto);

        UserDto actual = userController.updateUser(1L, userDto);

        assertEquals(userDto, actual, FAIL_USER_MESSAGE);
    }

    @Test
    void testDeleteUser() {
        Long userId = 0L;
        userController.deleteUser(userId);

        verify(userService).deleteUserById(userId);
    }
}