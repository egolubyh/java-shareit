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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    ArgumentCaptor<User> argumentCaptor;

    private User user;
    private UserDto userDto;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User();
        user.setId(userId);
        user.setName("user");
        user.setEmail("user@mail.ru");

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        lenient().when(userMapper.toUserDto(any(User.class)))
                .thenReturn(userDto);
        lenient().when(userMapper.toUser(any(UserDto.class)))
                .thenReturn(user);
        lenient().doCallRealMethod().when(userMapper)
                .updateUser(any(UserDto.class), Mockito.any(User.class));
    }

    @Test
    void addNewUser() {
        when(userRepository.save(user))
                .thenReturn(user);

        UserDto actualUser = userService.addNewUser(userDto);

        assertEquals(userDto.getId(), actualUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findUserById_whenUserFound_thenReturnedUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        UserDto actualUser = userService.findUserById(userId);

        assertEquals(user.getId(), actualUser.getId());
    }

    @Test
    void findUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(userId));
    }

    @Test
    void findAllUsers() {
        List<UserDto> expected = List.of(userDto);

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> result = userService.findAllUsers();

        assertEquals(1, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
    }

    @Test
    void updateUserById_whenUserFound_thenUpdatedOnlyAvailableFields() throws UserNotFoundException {
        UserDto newUserDto = new UserDto();
        newUserDto.setName("newName");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        lenient().when(userMapper.toUserDto(any(User.class)))
                .thenCallRealMethod();

        userService.updateUserById(userId, newUserDto);

        verify(userMapper).updateUser(newUserDto, user);
        verify(userRepository).save(argumentCaptor.capture());
        User savedUser = argumentCaptor.getValue();

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals("newName", savedUser.getName());
    }

    @Test
    void updateUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUserById(userId, new UserDto()));
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }
}