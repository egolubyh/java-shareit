package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_REQUESTOR_MESSAGE = "Возвращаемый requestor не соответствует ожидаемому";
    private static final String FAIL_ITEM_MESSAGE = "Возвращаемый item не соответствует ожидаемому";
    private static final String FAIL_CREATED_MESSAGE = "Возвращаемый created не соответствует ожидаемому";
    private static final String FAIL_DESCRIPTION_MESSAGE = "Возвращаемый description не соответствует ожидаемому";
    @Mock private UserRepository userRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private ItemMapper itemMapper;
    @Mock private CommentRepository commentRepository;
    @Mock private CommentMapper commentMapper;
    @InjectMocks
    private ItemRequestMapper itemRequestMapper;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);

        Mockito
                .lenient().when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .lenient().when(itemRepository.findByItemRequest(Mockito.any(ItemRequest.class)))
                .thenReturn(List.of(item));
        Mockito
                .lenient().when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .lenient().when(commentRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(comment));
        Mockito
                .lenient().when(itemMapper.toItemIdDto(Mockito.any(Item.class)))
                .thenCallRealMethod();
        Mockito
                .lenient().when(commentMapper.toCommentDto(Mockito.any(Comment.class)))
                .thenCallRealMethod();

        ReflectionTestUtils.setField(itemMapper, "commentRepository", commentRepository);
        ReflectionTestUtils.setField(itemMapper, "commentMapper", commentMapper);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("desc");
        itemRequestDto.setRequestorId(1L);
        itemRequestDto.setItems(List.of(itemMapper.toItemIdDto(item)));
        itemRequestDto.setCreated(LocalDateTime.now());

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("desc");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void testToItemRequest() throws UserNotFoundException {
        ItemRequest result = itemRequestMapper.toItemRequest(itemRequestDto);

        assertNotNull(result);
        assertEquals(itemRequestDto.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(itemRequestDto.getDescription(), result.getDescription(), FAIL_DESCRIPTION_MESSAGE);
        assertEquals(itemRequestDto.getRequestorId(), result.getRequestor().getId(), FAIL_REQUESTOR_MESSAGE);
        assertEquals(itemRequestDto.getCreated(), result.getCreated(), FAIL_CREATED_MESSAGE);
    }

    @Test
    void testToItemRequestDto() {
        ItemRequestDto result = itemRequestMapper.toItemRequestDto(itemRequest);

        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(itemRequest.getDescription(), result.getDescription(), FAIL_DESCRIPTION_MESSAGE);
        assertEquals(itemRequest.getRequestor().getId(), result.getRequestorId(), FAIL_REQUESTOR_MESSAGE);
        assertEquals(itemRequest.getCreated(), result.getCreated(), FAIL_CREATED_MESSAGE);
        assertEquals(1L, result.getItems().get(0).getId(), FAIL_ITEM_MESSAGE);
    }
}