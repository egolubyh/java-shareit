package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerITest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("desc");
        itemRequestDto.setRequestorId(1L);
        itemRequestDto.setItems(List.of(new ItemIdDto()));
        itemRequestDto.setCreated(LocalDateTime.now());
    }

    @SneakyThrows
    @Test
    void addNewItemRequest() {
        when(itemRequestService.createItemRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void addNewItemRequest_whenItemRequestNotValidDescriptionField_thenBadRequest() {
        itemRequestDto.setDescription("");

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(itemRequestService, never()).createItemRequest(any(ItemRequestDto.class),anyLong());
    }

    @SneakyThrows
    @Test
    void findItemRequestsByUser() {
        List<ItemRequestDto> expectedList = List.of(itemRequestDto);

        when(itemRequestService.readItemRequestByUser(anyLong()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @SneakyThrows
    @Test
    void findItemRequestById() {
        when(itemRequestService.readItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        String result = mockMvc.perform(get("/requests/{requestId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    void findAllItemRequest() {
        List<ItemRequestDto> expectedList = List.of(itemRequestDto);

        when(itemRequestService.readAll(anyLong(),anyInt(),anyInt()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }
}