package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private Long userId;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userId = 1L;

        userDto = new UserDto();
        userDto.setName("user");
        userDto.setEmail("user@mail.ru");
    }

    @SneakyThrows
    @Test
    void addNewUser() {
        when(userService.addNewUser(any(UserDto.class)))
                .thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void addNewUser_whenUserNotValidEmailField_thenBadRequest() {
        userDto.setEmail(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addNewUser(any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void addNewUser_whenUserNotValidNameField_thenBadRequest() {
        userDto.setName(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addNewUser(any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void findUser() {
        when(userService.findUserById(anyLong()))
                .thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void findAllUsers() {
        List<UserDto> expectedList = List.of(userDto);
        when(userService.findAllUsers()).thenReturn(expectedList);

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        when(userService.updateUserById(anyLong(), any(UserDto.class)))
                .thenReturn(userDto);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).deleteUserById(userId);
    }
}