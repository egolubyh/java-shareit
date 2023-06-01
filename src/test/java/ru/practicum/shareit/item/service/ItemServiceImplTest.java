package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.exception.BadOwnerException;
import ru.practicum.shareit.item.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock private ItemMapper itemMapper;
    @Mock private CommentMapper commentMapper;
    @Mock private ItemRepository itemRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private BookingMapper bookingMapper;

    @InjectMocks
    ItemServiceImpl itemService;
    @Captor
    ArgumentCaptor<Item> itemArgumentCaptor;
    @Captor
    ArgumentCaptor<Comment> commentArgumentCaptor;

    private Long ownerId;
    private Long userId;
    private User owner;
    private User user;
    private Item item;
    private Comment comment;


    @BeforeEach
    void setUp() {
        ownerId = 1L;
        userId = 2L;

        owner = new User();
        owner.setId(ownerId);
        owner.setName("owner");
        owner.setEmail("owner@mail.ru");

        user = new User();
        user.setId(userId);
        user.setName("user");
        user.setEmail("user@mail.ru");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setOwner(owner);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        ReflectionTestUtils.setField(commentMapper, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(commentMapper, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemMapper, "commentRepository", commentRepository);
        ReflectionTestUtils.setField(itemMapper, "commentMapper", commentMapper);

        lenient().when(itemMapper.toItem(isA(ItemDto.class))).thenCallRealMethod();
        lenient().when(itemMapper.toItemDto(isA(Item.class))).thenCallRealMethod();
        lenient().when(itemMapper.toItemIdDto(isA(Item.class))).thenCallRealMethod();
        lenient().doCallRealMethod().when(itemMapper).updateItem(any(ItemDto.class), any(Item.class));

        lenient().when(commentMapper.toComment(isA(CommentDto.class))).thenCallRealMethod();
        lenient().when(commentMapper.toCommentDto(isA(Comment.class))).thenCallRealMethod();
    }

    @Test
    void addNewItem_whenOwnerIdFound_thenSaveNewItem() throws UserNotFoundException {
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        itemService.addNewItem(itemDto, ownerId);

        verify(itemRepository).save(itemArgumentCaptor.capture());

        Item actualItem = itemArgumentCaptor.getValue();

        assertEquals(item.getName(), actualItem.getName());
        assertEquals(item.getDescription(), actualItem.getDescription());
        assertEquals(item.getAvailable(), item.getAvailable());
        assertEquals(item.getOwner(), item.getOwner());
    }

    @Test
    void addNewItem_whenOwnerIdNotFound_thenUserNotFoundExceptionThrow() {
        ItemDto itemDto = itemMapper.toItemDto(item);
        when(userRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> itemService.addNewItem(itemDto, ownerId));
    }

    @Test
    void addNewComment_whenUserIdAndItemIdFound_thenSaveNewItem() throws UserNotFoundException, BookingNotFoundException, ItemNotFoundException {
        CommentDto commentDto = commentMapper.toCommentDto(comment);

        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        lenient().when(bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(
                        any(User.class), any(Item.class), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new Booking()));

        itemService.addNewComment(commentDto, item.getId(), userId);

        verify(commentRepository).save(commentArgumentCaptor.capture());
        Comment actualComment = commentArgumentCaptor.getValue();

        assertEquals(comment.getId(), actualComment.getId());
        assertEquals(comment.getText(), actualComment.getText());
        assertEquals(comment.getAuthor(), actualComment.getAuthor());
        assertEquals(comment.getItem(), actualComment.getItem());
        assertEquals(comment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-d_H:m")),
                actualComment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-d_H:m")));

    }

    @Test
    void addNewComment_whenUserIdNotFound_thenUserNotFoundExceptionThrow() {
        CommentDto commentDto = commentMapper.toCommentDto(comment);
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(UserNotFoundException.class,
                () -> itemService.addNewComment(commentDto, item.getId(), userId));
    }

    @Test
    void addNewComment_whenItemIdNotFound_thenItemNotFoundExceptionThrow() {
        CommentDto commentDto = commentMapper.toCommentDto(comment);
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> itemService.addNewComment(commentDto, item.getId(), userId));
    }

    @Test
    void addNewComment_whenBookingNotFound_thenBookingNotFoundExceptionThrow() {
        CommentDto commentDto = commentMapper.toCommentDto(comment);
        lenient().when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        lenient().when(bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(
                        any(User.class), any(Item.class), any(Status.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> itemService.addNewComment(commentDto, item.getId(), userId));
    }

    @Test
    void readItem_whenItemIdFound_thenReturnedItem() throws ItemNotFoundException {
        lenient().when(commentRepository.findByItem(any(Item.class)))
                .thenReturn(List.of(comment));

        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        ItemIdDto expected = itemMapper.toItemIdDto(item);
        ItemIdDto actualItemIdDto = itemService.readItem(item.getId(), userId);

        assertEquals(expected.getId(), actualItemIdDto.getId());
        assertEquals(expected.getName(), actualItemIdDto.getName());
        assertEquals(expected.getDescription(), actualItemIdDto.getDescription());
        assertEquals(expected.getAvailable(), actualItemIdDto.getAvailable());
        assertEquals(expected.getRequestId(), actualItemIdDto.getRequestId());
        assertEquals(expected.getComments().get(0).getId(),
                actualItemIdDto.getComments().get(0).getId());
    }

    @Test
    void readItem_whenItemIdNotFound_thenItemNotFoundExceptionThrow() {
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> itemService.readItem(item.getId(), userId));
    }

    @Test
    void readAllItemByOwner_whenOwnerIdFound_thenReturnedItems() throws UserNotFoundException {
        List<ItemIdDto> expected = List.of(itemMapper.toItemIdDto(item));

        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        lenient().when(itemRepository.findByOwnerOrderByIdAsc(any(User.class)))
                .thenReturn(List.of(item));

        List<ItemIdDto> result = itemService.readAllItemByOwner(ownerId);

        assertEquals(expected, result);
    }

    @Test
    void readAllItemByOwner_whenOwnerIdNotFound_thenUserNotFoundExceptionThrows() {
        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> itemService.readAllItemByOwner(ownerId));
    }

    @Test
    void readAllItemByParam_whenParamIsNotEmpty_thenReturnedItems() {
        List<ItemDto> expected = List.of(itemMapper.toItemDto(item));

        lenient().when(itemRepository.search(anyString()))
                .thenReturn(List.of(item));

        List<ItemDto> actual = itemService.readAllItemByParam("text");

        assertEquals(expected, actual);
    }

    @Test
    void readAllItemByParam_whenParamIsEmpty_thenReturnedEmptyList() {
        List<ItemDto> expected = Collections.emptyList();

        lenient().when(itemRepository.search(""))
                .thenReturn(Collections.emptyList());

        List<ItemDto> actual = itemService.readAllItemByParam("");

        assertEquals(expected, actual);
    }

    @Test
    void updateItem_whenItemIdAndOwnerIdFound_thenReturnedItem() throws BadOwnerException, ItemNotFoundException {
        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setName("updateName");
        itemDto.setDescription("updateDesc");

        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        itemService.updateItem(itemDto, item.getId(), ownerId);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item actual = itemArgumentCaptor.getValue();

        assertEquals(itemDto.getName(), actual.getName());
        assertEquals(itemDto.getDescription(), actual.getDescription());

    }

    @Test
    void updateItem_whenItemIdNotFound_thenItemNotFoundExceptionThrow() {
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(new ItemDto(), item.getId(), ownerId));
    }

    @Test
    void updateItem_whenBadOwner_thenBadOwnerExceptionThrow() {
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(BadOwnerException.class,
                () -> itemService.updateItem(new ItemDto(), item.getId(), userId));
    }
}