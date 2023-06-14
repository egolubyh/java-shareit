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
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_SIZE_MESSAGE = "Возвращаемый список имеет размер не соответствует ожидаемому";
    private static final String FAIL_EMAIL_MESSAGE = "Возвращаемый email не соответствует ожидаемому";
    private static final String FAIL_NAME_MESSAGE = "Возвращаемый name не соответствует ожидаемому";
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
    void testAddNewUser() {
        when(userRepository.save(user))
                .thenReturn(user);

        UserDto actualUser = userService.addNewUser(userDto);

        assertEquals(userDto.getId(), actualUser.getId(), FAIL_ID_MESSAGE);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserById_whenUserFound_thenReturnedUser() throws UserNotFoundException {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        UserDto actualUser = userService.findUserById(userId);

        assertEquals(user.getId(), actualUser.getId(), FAIL_ID_MESSAGE);
    }

    @Test
    void testFindUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(userId));
    }

    @Test
    void testFindAllUsers() {
        List<UserDto> expected = List.of(userDto);

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> result = userService.findAllUsers();

        assertEquals(1, result.size(), FAIL_SIZE_MESSAGE);
        assertEquals(expected.get(0).getId(), result.get(0).getId(), FAIL_ID_MESSAGE);
    }

    @Test
    void testUpdateUserById_whenUserFound_thenUpdatedOnlyAvailableFields() throws UserNotFoundException {
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

        assertEquals(user.getId(), savedUser.getId(), FAIL_ID_MESSAGE);
        assertEquals(user.getEmail(), savedUser.getEmail(), FAIL_EMAIL_MESSAGE);
        assertEquals("newName", savedUser.getName(), FAIL_NAME_MESSAGE);
    }

    @Test
    void testUpdateUserById_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUserById(userId, new UserDto()));
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }
}