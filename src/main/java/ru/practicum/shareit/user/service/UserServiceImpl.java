package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.InvalidEmailException;
import ru.practicum.shareit.user.exception.NoEmailException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addNewUser(UserDto userDto)
            throws DuplicateEmailException, NoEmailException, InvalidEmailException {
        if (userDto.getEmail() == null) throw new NoEmailException("Email is null");
        if (!userDto.getEmail().contains("@")) throw new InvalidEmailException("Invalid Email");
        validEmail(userDto.getEmail());
        User user = userRepository.saveUser(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findUserById(userId);
        return userMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserById(Long userId, UserDto userDto) throws DuplicateEmailException, InvalidEmailException {
        if (userDto.getEmail() != null && !userDto.getEmail().contains("@")) throw new InvalidEmailException("Invalid Email");
        validEmail(userDto.getEmail());
        User user = userRepository.findUserById(userId);
        userMapper.updateUser(userDto, user);
        User updateUser = userRepository.updateUserById(userId, user);
        return userMapper.toUserDto(updateUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteUserById(userId);
    }

    private void validEmail(String email) throws DuplicateEmailException {
        boolean exist = userRepository.findAllUsers().stream()
                .map(User::getEmail)
                .anyMatch(Predicate.isEqual(email));

        if (exist) throw new DuplicateEmailException(email);
    }
}
