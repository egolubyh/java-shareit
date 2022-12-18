package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, Long ownerId) throws UserNotFoundException, NoNameException, NoAvailableException, NoDescriptionException;

    ItemDto readItem(Long itemId, Long ownerId);

    List<ItemDto> readAllItemByOwner(Long ownerId);

    List<ItemDto> readAllItemByParam(String search);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) throws BadOwnerException;
}