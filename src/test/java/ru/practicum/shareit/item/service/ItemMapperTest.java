package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_NAME_MESSAGE = "Возвращаемый name не соответствует ожидаемому";
    private static final String FAIL_DESCRIPTION_MESSAGE = "Возвращаемый description не соответствует ожидаемому";
    private static final String FAIL_AVAILABLE_MESSAGE = "Возвращаемый available не соответствует ожидаемому";
    private static final String FAIL_ITEM_REQUEST_MESSAGE = "Возвращаемый ItemRequest не соответствует ожидаемому";
    private static final String FAIL_COMMENTS_MESSAGE = "Возвращаемый список comments не соответствует ожидаемому";
    private static final String FAIL_REQUEST_MESSAGE = "Возвращаемый список request не соответствует ожидаемому";
    @Mock private CommentRepository commentRepository;
    @Mock private ItemRequestRepository itemRequestRepository;
    @Mock private CommentMapper commentMapper;
    @InjectMocks
    private ItemMapper itemMapper;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        Mockito
                .lenient().when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.of(itemRequest));

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);
        item.setItemRequest(itemRequest);
        item.setOwner(user);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setItem(item);
        comment.setAuthor(user);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setItemId(item.getId());
        commentDto.setAuthorName(user.getName());

        Mockito
                .lenient().when(commentMapper.toCommentDto(comment))
                .thenReturn(commentDto);
        Mockito
                .lenient().when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
    }

    @Test
    void testToItemDto() {
        ItemDto result = itemMapper.toItemDto(item);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(item.getName(), result.getName(), FAIL_NAME_MESSAGE);
        assertEquals(item.getDescription(), result.getDescription(), FAIL_DESCRIPTION_MESSAGE);
        assertEquals(item.getAvailable(), result.getAvailable(), FAIL_AVAILABLE_MESSAGE);
        assertEquals(item.getItemRequest().getId(), result.getRequestId(), FAIL_ITEM_REQUEST_MESSAGE);
    }

    @Test
    void testToItemIdDto() {
        ItemIdDto result = itemMapper.toItemIdDto(item);
        List<Long> commentIds = commentRepository.findByItem(item).stream()
                .map(Comment::getId)
                .collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(item.getName(), result.getName(), FAIL_NAME_MESSAGE);
        assertEquals(item.getDescription(), result.getDescription(), FAIL_DESCRIPTION_MESSAGE);
        assertEquals(item.getAvailable(), result.getAvailable(), FAIL_AVAILABLE_MESSAGE);
        assertEquals(item.getItemRequest().getId(), result.getRequestId(), FAIL_ITEM_REQUEST_MESSAGE);
        assertEquals(commentIds, result.getComments().stream()
                .map(CommentDto::getId)
                .collect(Collectors.toList()), FAIL_COMMENTS_MESSAGE);
    }

    @Test
    void testToItem() {
        Item result = itemMapper.toItem(itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName(), FAIL_NAME_MESSAGE);
        assertEquals(itemDto.getDescription(), result.getDescription(), FAIL_DESCRIPTION_MESSAGE);
        assertEquals(itemDto.getAvailable(), result.getAvailable(), FAIL_AVAILABLE_MESSAGE);
        assertEquals(itemDto.getRequestId(), result.getItemRequest().getId(), FAIL_REQUEST_MESSAGE);
    }

    @Test
    void testUpdateItemName() {
        ItemDto updateItemDto = new ItemDto();
        updateItemDto.setName("update");

        itemMapper.updateItem(updateItemDto, item);

        assertNotNull(item);
        assertNotNull(updateItemDto);
        assertEquals(updateItemDto.getName(), item.getName(), FAIL_NAME_MESSAGE);
    }

    @Test
    void testUpdateItemDescription() {
        ItemDto updateItemDto = new ItemDto();
        updateItemDto.setDescription("updateDesc");

        itemMapper.updateItem(updateItemDto, item);

        assertNotNull(item);
        assertNotNull(updateItemDto);
        assertEquals(updateItemDto.getDescription(), item.getDescription(), FAIL_DESCRIPTION_MESSAGE);
    }

    @Test
    void testUpdateItemAvailable() {
        ItemDto updateItemDto = new ItemDto();
        updateItemDto.setAvailable(false);

        itemMapper.updateItem(updateItemDto, item);

        assertNotNull(item);
        assertNotNull(updateItemDto);
        assertEquals(updateItemDto.getAvailable(), item.getAvailable(), FAIL_AVAILABLE_MESSAGE);
    }
}