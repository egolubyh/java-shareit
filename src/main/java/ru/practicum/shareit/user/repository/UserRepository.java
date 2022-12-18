package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);

    User findUserById(Long userId);

    List<User> findAllUsers();

    User updateUserById(Long userId, User user);

    void deleteUserById(Long userId);
}