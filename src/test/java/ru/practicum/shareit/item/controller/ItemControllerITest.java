package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerITest {
    private static final String FAIL_ITEM_MESSAGE = "Возвращаемый Item не соответствует ожидаемому";
    private static final String FAIL_COMMENT_MESSAGE = "Возвращаемый Comment не соответствует ожидаемому";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Long itemId;
    private ItemDto itemDto;
    private ItemIdDto itemIdDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemId = 1L;

        itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(Boolean.TRUE);

        commentDto = new CommentDto();
        commentDto.setText("text");
        commentDto.setItemId(itemId);
        commentDto.setAuthorName("user");
        commentDto.setAuthorId(1L);
        commentDto.setCreated(LocalDateTime.now());

        itemIdDto = new ItemIdDto();
        itemIdDto.setName(itemDto.getName());
        itemIdDto.setDescription(itemDto.getDescription());
        itemIdDto.setAvailable(itemDto.getAvailable());
        itemIdDto.setComments(List.of(commentDto));
    }

    @SneakyThrows
    @Test
    void testAddItem() {
        when(itemService.addNewItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testAddItem_whenItemNotValidNameField_thenBadRequest() {
        itemDto.setName(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addNewItem(any(ItemDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void testAddItem_whenItemNotValidDescriptionField_thenBadRequest() {
        itemDto.setDescription(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addNewItem(any(ItemDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void testAddItem_whenItemNotValidAvailableField_thenBadRequest() {
        itemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addNewItem(any(ItemDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void testAddComment() {
        when(itemService.addNewComment(any(CommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result, FAIL_COMMENT_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testAddComment_whenCommentNotValidTextField() {
        commentDto.setText(null);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addNewComment(any(CommentDto.class), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void testUpdateItem() {
        when(itemService.updateItem(any(ItemDto.class),anyLong(), anyLong()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemById() {
        when(itemService.readItem(anyLong(), anyLong()))
                .thenReturn(itemIdDto);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemIdDto), result, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemByOwner() {
        List<ItemIdDto> expectedList = List.of(itemIdDto);
        when(itemService.readAllItemByOwner(anyLong()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemByParam() {
        List<ItemDto> expectedList = List.of(itemDto);
        when(itemService.readAllItemByParam(anyString()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/items/search")
                        .param("text", "name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result, FAIL_ITEM_MESSAGE);
    }
}