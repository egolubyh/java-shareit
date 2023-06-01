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
    void addItem() {
        when(itemService.addNewItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        ItemDto actual = itemController.addItem(itemDto, 1L);

        assertEquals(itemDto, actual);
    }

    @SneakyThrows
    @Test
    void addComment() {
        when(itemService.addNewComment(any(CommentDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        CommentDto actual = itemController.addComment(commentDto, 1L,1L);

        assertEquals(commentDto, actual);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        ItemDto actual = itemController.updateItem(itemDto, 1L,1L);

        assertEquals(itemDto, actual);
    }

    @SneakyThrows
    @Test
    void findItemById() {
        when(itemService.readItem(anyLong(), anyLong()))
                .thenReturn(itemIdDto);

        ItemIdDto actual = itemController.findItemById(1L,1L);

        assertEquals(itemIdDto, actual);
    }

    @SneakyThrows
    @Test
    void findItemByOwner() {
        List<ItemIdDto> expectedList = List.of(itemIdDto);
        when(itemService.readAllItemByOwner(anyLong()))
                .thenReturn(expectedList);

        List<ItemIdDto> actual = itemController.findItemByOwner(1L);

        assertEquals(expectedList, actual);
    }

    @SneakyThrows
    @Test
    void findItemByParam() {
        List<ItemDto> expectedList = List.of(itemDto);
        when(itemService.readAllItemByParam(anyString()))
                .thenReturn(expectedList);

        List<ItemDto> actual = itemController.findItemByParam("text");

        assertEquals(expectedList, actual);
    }
}