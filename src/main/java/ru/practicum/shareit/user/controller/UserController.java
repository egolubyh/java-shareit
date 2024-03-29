package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@RequestBody @Valid UserDto user) {
        log.info("Получен запрос к эндпоинту: /users, метод POST");

        return userService.addNewUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable Long userId)
            throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{}, метод GET", userId);

        return userService.findUserById(userId);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.info("Получен запрос к эндпоинту: /users, метод GET");

        return userService.findAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody UserDto user) throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{}, метод PATCH", userId);

        return userService.updateUserById(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: /users/{}, метод DELETE", userId);

        userService.deleteUserById(userId);
    }
}