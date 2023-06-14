package ru.practicum.shareit.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_TEXT_MESSAGE = "Возвращаемый text не соответствует ожидаемому";
    private static final String FAIL_NAME_MESSAGE = "Возвращаемый name не соответствует ожидаемому";
    private static final String FAIL_ITEM_ID_MESSAGE = "Возвращаемый itemId не соответствует ожидаемому";
    private static final String FAIL_USER_ID_MESSAGE = "Возвращаемый userId не соответствует ожидаемому";
    private static final String FAIL_CREATED_MESSAGE = "Возвращаемый created не соответствует ожидаемому";
    private static final Long USER_ID = 22L;
    private static final Long ITEM_ID = 33L;
    private static final LocalDateTime CREATED = LocalDateTime.now();
    @Mock private UserRepository userRepository;
    @Mock private ItemRepository itemRepository;
    @InjectMocks
    private CommentMapper commentMapper;
    private CommentDto commentDto;
    private Comment comment;


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(USER_ID);
        user.setName("name");
        Item item = new Item();
        item.setId(ITEM_ID);

        Mockito
                .lenient().when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));
        Mockito
                .lenient().when(itemRepository.findById(ITEM_ID))
                .thenReturn(Optional.of(item));

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setItemId(ITEM_ID);
        commentDto.setAuthorId(USER_ID);
        commentDto.setCreated(CREATED);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(CREATED);
    }

    @Test
    void testToComment() {
        Comment result = commentMapper.toComment(commentDto);

        assertNotNull(result);
        assertEquals(1L, result.getId(), FAIL_ID_MESSAGE);
        assertEquals("text", result.getText(), FAIL_TEXT_MESSAGE);
        assertEquals(ITEM_ID, result.getItem().getId(), FAIL_ITEM_ID_MESSAGE);
        assertEquals(USER_ID, result.getAuthor().getId(), FAIL_USER_ID_MESSAGE);
        assertEquals(CREATED, result.getCreated(), FAIL_CREATED_MESSAGE);
    }

    @Test
    void testToCommentDto() {
        CommentDto result = commentMapper.toCommentDto(comment);

        assertNotNull(result);
        assertEquals(1L, result.getId(), FAIL_ID_MESSAGE);
        assertEquals("text", result.getText(), FAIL_TEXT_MESSAGE);
        assertEquals("name", result.getAuthorName(), FAIL_NAME_MESSAGE);
        assertEquals(ITEM_ID, result.getItemId(), FAIL_ITEM_ID_MESSAGE);
        assertEquals(CREATED, result.getCreated(), FAIL_CREATED_MESSAGE);
    }
}