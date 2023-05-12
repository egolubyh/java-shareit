package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, Long ownerId) throws UserNotFoundException;

    ItemIdDto readItem(Long itemId, Long ownerId) throws ItemNotFoundException;

    List<ItemIdDto> readAllItemByOwner(Long ownerId) throws UserNotFoundException;

    List<ItemDto> readAllItemByParam(String search);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId)
            throws BadOwnerException, ItemNotFoundException;

    CommentDto addNewComment(CommentDto commentDto, Long itemId, Long userId)
            throws ItemNotFoundException, UserNotFoundException, BookingNotFoundException;
}