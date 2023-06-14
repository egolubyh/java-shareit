package ru.practicum.shareit.request.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    private static final String FAIL_ITEM_REQUEST_MESSAGE = "Возвращаемый itemRequest не соответствует ожидаемому";
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
    }

    @SneakyThrows
    @Test
    void addNewItemRequest() {
        when(itemRequestService.createItemRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        ItemRequestDto actual = itemRequestController.addNewItemRequest(itemRequestDto, 1L);

        assertEquals(itemRequestDto, actual, FAIL_ITEM_REQUEST_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemRequestsByUser() {
        List<ItemRequestDto> expectedList = List.of(itemRequestDto);
        when(itemRequestService.readItemRequestByUser(anyLong()))
                .thenReturn(expectedList);

        List<ItemRequestDto> actual = itemRequestController.findItemRequestsByUser(1L);

        assertEquals(expectedList, actual, FAIL_ITEM_REQUEST_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindItemRequestById() {
        when(itemRequestService.readItemRequestById(anyLong(),anyLong()))
                .thenReturn(itemRequestDto);

        ItemRequestDto actual = itemRequestController.findItemRequestById(1L, 1L);

        assertEquals(itemRequestDto, actual, FAIL_ITEM_REQUEST_MESSAGE);
    }

    @SneakyThrows
    @Test
    void testFindAllItemRequest() {
        List<ItemRequestDto> expectedList = List.of(itemRequestDto);
        when(itemRequestService.readAll(anyLong(),anyInt(),anyInt()))
                .thenReturn(expectedList);

        List<ItemRequestDto> actual = itemRequestController.findAllItemRequest(1L,1,1);

        assertEquals(expectedList, actual, FAIL_ITEM_REQUEST_MESSAGE);
    }
}