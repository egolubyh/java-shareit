package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    ArgumentCaptor<User> argumentCaptor;

    private User user1;
    private User user2;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user1 = new User();
        user1.setId(userId);
        user1.setName("user");
        user1.setEmail("user@mail.ru");

        user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        Mockito
                .lenient().when(userMapper.toUserDto(Mockito.any(User.class)))
                .thenCallRealMethod();
        Mockito
                .lenient().when(userMapper.toUser(Mockito.any(UserDto.class)))
                .thenCallRealMethod();
        Mockito
                .lenient().doCallRealMethod().when(userMapper).updateUser(Mockito.any(UserDto.class), Mockito.any(User.class));
    }

    @Test
    void addNewUser() {
        UserDto userDto = userMapper.toUserDto(user1);
        Mockito
                .when(userRepository.save(user1))
                .thenReturn(user1);

        UserDto actualUser = userService.addNewUser(userDto);

        assertEquals(userDto, actualUser);
        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void findUserById_whenUserFound_thenReturnedUser() throws UserNotFoundException {
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.of(user1));

        User actualUser = userMapper.toUser(userService.findUserById(userId));

        assertEquals(user1, actualUser);
    }

    @Test
    void findUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(userId));
    }

    @Test
    void findAllUsers() {
        UserDto userDto1 = userMapper.toUserDto(user1);
        UserDto userDto2 = userMapper.toUserDto(user2);

        List<UserDto> expected = List.of(userDto1, userDto2);

        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.findAllUsers();

        assertEquals(expected, result);
    }

    @Test
    void updateUserById_whenUserFound_thenUpdatedOnlyAvailableFields() throws UserNotFoundException {
        User newUser = new User();
        newUser.setName("newName");

        UserDto newUserDto = userMapper.toUserDto(newUser);

        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.of(user1));

        userService.updateUserById(userId, newUserDto);

        Mockito.verify(userRepository).save(argumentCaptor.capture());
        User savedUser = argumentCaptor.getValue();

        assertEquals(user1.getId(), savedUser.getId());
        assertEquals(user1.getEmail(), savedUser.getEmail());
        assertEquals("newName", savedUser.getName());
    }

    @Test
    void updateUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        Mockito
                .when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUserById(userId, new UserDto()));
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(userId);

        Mockito
                .verify(userRepository).deleteById(Mockito.any());
    }
}