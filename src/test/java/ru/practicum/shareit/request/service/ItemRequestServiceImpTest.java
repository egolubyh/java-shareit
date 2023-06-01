package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImpTest {
    @Mock private ItemRequestRepository itemRequestRepository;
    @Mock private UserRepository userRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemRequestServiceImp itemRequestService;

    @Captor
    ArgumentCaptor<ItemRequest> argumentCaptor;
    private Long userId;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private Item item;

    @BeforeEach
    void setUp() throws UserNotFoundException {
        userId = 1L;
        user = new User();
        user.setId(userId);
        user.setName("user");
        user.setEmail("user@mail.ru");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setRequestorId(userId);
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);

        lenient().when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class)))
                .thenReturn(itemRequest);
        lenient().when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto);
    }

    @Test
    void createItemRequest() throws UserNotFoundException {
        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        lenient().when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequestDto actual = itemRequestService.createItemRequest(itemRequestDto, userId);

        verify(itemRequestRepository).save(argumentCaptor.capture());

        assertEquals(itemRequest.getId(), argumentCaptor.getValue().getId());
        assertEquals(itemRequest.getId(), actual.getId());
    }

    @Test
    void readItemRequestByUser_whenUserIdFound_thenReturnItemRequestDto() throws UserNotFoundException {
        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        lenient().when(itemRequestRepository.findByRequestorOrderByCreatedDesc(user))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> result = itemRequestService.readItemRequestByUser(userId);

        assertEquals(itemRequest.getId(), result.get(0).getId());
        assertEquals(1, result.size());
    }

    @Test
    void readItemRequestByUser_whenUserIdNotFound_thenUserNotFoundExceptionThrow() {
        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> itemRequestService.readItemRequestByUser(userId));
    }

    @Test
    void readItemRequestById_whenUserIdAndRequestIdFound_thenReturnedItemRequestDto() throws UserNotFoundException, RequestNotFoundException {
        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        lenient().when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequestDto actual = itemRequestService.readItemRequestById(userId, itemRequest.getId());

        assertEquals(itemRequest.getId(), actual.getId());
    }

    @Test
    void readItemRequestById_whenUserIdNotFound_thenUserNotFoundExceptionThrow() {
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        lenient().when(itemRequestRepository.findById(itemRequest.getId()))
                .thenReturn(Optional.of(itemRequest));

        assertThrows(UserNotFoundException.class,
                () -> itemRequestService.readItemRequestById(userId,1L));
    }

    @Test
    void readItemRequestById_whenRequestIdNotFound_thenRequestNotFoundExceptionThrow() {
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        lenient().when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class,
                () -> itemRequestService.readItemRequestById(userId,1L));
    }

    @Test
    void readAll_whenUserIdFound_thenReturnedItemRequestDto() throws UserNotFoundException {
        Page<ItemRequest> itemRequests = new PageImpl<>(List.of(itemRequest));

        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        lenient().when(itemRequestRepository.findAllByRequestorNot(
                any(User.class), any(PageRequest.class)))
                .thenReturn(itemRequests);
        List<ItemRequestDto> actual = itemRequests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        List<ItemRequestDto> result = itemRequestService.readAll(userId,0,10);

        assertEquals(actual,result);
    }

    @Test
    void readAll_whenUserIdNotFound_thenUserNotFoundExceptionThrow() {
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> itemRequestService.readAll(userId, 0, 10));
    }
}