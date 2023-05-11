package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new UserNotFoundException("Пользователя с id = " + userId + " не существует."));

        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto updateUserById(Long userId, UserDto updatedUserDto) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new UserNotFoundException("Пользователя с id = " + userId + " не существует."));

        user.setId(userId);

        userMapper.updateUser(updatedUserDto,user);
        User updateUser = userRepository.save(user);

        return userMapper.toUserDto(updateUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}