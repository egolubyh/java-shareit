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
    void toComment() {
        Comment result = commentMapper.toComment(commentDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("text", result.getText());
        assertEquals(ITEM_ID, result.getItem().getId());
        assertEquals(USER_ID, result.getAuthor().getId());
        assertEquals(CREATED, result.getCreated());
    }

    @Test
    void toCommentDto() {
        CommentDto result = commentMapper.toCommentDto(comment);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("text", result.getText());
        assertEquals("name", result.getAuthorName());
        assertEquals(ITEM_ID, result.getItemId());
        assertEquals(CREATED, result.getCreated());
    }
}