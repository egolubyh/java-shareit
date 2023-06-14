package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) throws UserNotFoundException;

    List<ItemRequestDto> readItemRequestByUser(Long userId) throws UserNotFoundException;

    ItemRequestDto readItemRequestById(Long userId, Long requestId) throws UserNotFoundException, RequestNotFoundException;

    List<ItemRequestDto> readAll(Long userId, Integer from, Integer size) throws UserNotFoundException;
}