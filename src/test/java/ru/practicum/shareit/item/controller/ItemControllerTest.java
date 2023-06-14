package ru.practicum.shareit.item.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    private static final String FAIL_ITEM_MESSAGE = "Возвращаемый Item не соответствует ожидаемому";
    private static final String FAIL_COMMENT_MESSAGE = "Возвращаемый Comment не соответствует ожидаемому";
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    private ItemDto itemDto;
    private CommentDto commentDto;
    private ItemIdDto itemIdDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        commentDto = new CommentDto();
        itemIdDto = new ItemIdDto();
    }

    @SneakyThrows
    @Test
    void testAddItem() {
        when(itemService.addNewItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        ItemDto actual = itemController.addItem(itemDto, 1L);

        assertEquals(itemDto, actual, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testAddComment() {
        when(itemService.addNewComment(any(CommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        CommentDto actual = itemController.addComment(commentDto, 1L,1L);

        assertEquals(commentDto, actual, FAIL_COMMENT_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testUpdateItem() {
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        ItemDto actual = itemController.updateItem(itemDto, 1L,1L);

        assertEquals(itemDto, actual, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemById() {
        when(itemService.readItem(anyLong(), anyLong()))
                .thenReturn(itemIdDto);

        ItemIdDto actual = itemController.findItemById(1L,1L);

        assertEquals(itemIdDto, actual, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemByOwner() {
        List<ItemIdDto> expectedList = List.of(itemIdDto);
        when(itemService.readAllItemByOwner(anyLong()))
                .thenReturn(expectedList);

        List<ItemIdDto> actual = itemController.findItemByOwner(1L);

        assertEquals(expectedList, actual, FAIL_ITEM_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemByParam() {
        List<ItemDto> expectedList = List.of(itemDto);
        when(itemService.readAllItemByParam(anyString()))
                .thenReturn(expectedList);

        List<ItemDto> actual = itemController.findItemByParam("text");

        assertEquals(expectedList, actual, FAIL_ITEM_MESSAGE);
    }
}