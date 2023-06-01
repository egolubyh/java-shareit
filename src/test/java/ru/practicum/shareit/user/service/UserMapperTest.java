package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
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
    void toUserDto() {
        UserDto resultDto = userMapper.toUserDto(user);

        assertNotNull(resultDto);
        assertEquals(user.getId(), resultDto.getId());
        assertEquals(user.getName(), resultDto.getName());
        assertEquals(user.getEmail(), resultDto.getEmail());
    }

    @Test
    void toUser() {
        User result = userMapper.toUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUserName() {
        UserDto updateUser = new UserDto();
        String updateName = "updateName";
        updateUser.setName(updateName);

        userMapper.updateUser(updateUser,user);

        assertNotNull(user);
        assertNotNull(updateUser);
        assertEquals(updateName, user.getName());
        assertEquals("user@user.ru", user.getEmail());
    }

    @Test
    void updateUserEmail() {
        UserDto updateUser = new UserDto();
        String updateEmail = "updateEmail@user.ru";
        updateUser.setEmail(updateEmail);

        userMapper.updateUser(updateUser,user);

        assertNotNull(user);
        assertNotNull(updateUser);
        assertEquals("User", user.getName());
        assertEquals(updateEmail, user.getEmail());
    }
}