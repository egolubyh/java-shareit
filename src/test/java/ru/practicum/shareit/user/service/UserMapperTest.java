package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_NAME_MESSAGE = "Возвращаемый name не соответствует ожидаемому";
    private static final String FAIL_EMAIL_MESSAGE = "Возвращаемый email не соответствует ожидаемому";
    private UserMapper userMapper;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();

        user = new User();
        user.setId(1L);
        user.setName("User");
        user.setEmail("user@user.ru");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("User");
        userDto.setEmail("user@user.ru");

    }

    @Test
    void testToUserDto() {
        UserDto resultDto = userMapper.toUserDto(user);

        assertNotNull(resultDto);
        assertEquals(user.getId(), resultDto.getId(), FAIL_ID_MESSAGE);
        assertEquals(user.getName(), resultDto.getName(), FAIL_NAME_MESSAGE);
        assertEquals(user.getEmail(), resultDto.getEmail(), FAIL_EMAIL_MESSAGE);
    }

    @Test
    void testToUser() {
        User result = userMapper.toUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(userDto.getName(), result.getName(), FAIL_NAME_MESSAGE);
        assertEquals(userDto.getEmail(), result.getEmail(), FAIL_EMAIL_MESSAGE);
    }

    @Test
    void testUpdateUserName() {
        UserDto updateUser = new UserDto();
        String updateName = "updateName";
        updateUser.setName(updateName);

        userMapper.updateUser(updateUser,user);

        assertNotNull(user);
        assertNotNull(updateUser);
        assertEquals(updateName, user.getName(), FAIL_NAME_MESSAGE);
        assertEquals("user@user.ru", user.getEmail(), FAIL_EMAIL_MESSAGE);
    }

    @Test
    void testUpdateUserEmail() {
        UserDto updateUser = new UserDto();
        String updateEmail = "updateEmail@user.ru";
        updateUser.setEmail(updateEmail);

        userMapper.updateUser(updateUser,user);

        assertNotNull(user);
        assertNotNull(updateUser);
        assertEquals("User", user.getName(), FAIL_NAME_MESSAGE);
        assertEquals(updateEmail, user.getEmail(), FAIL_EMAIL_MESSAGE);
    }
}