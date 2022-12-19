package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item saveItem(Item item);

    Item findItemById(Long itemId);

    List<Item> findItemByOwner(Long ownerId);

    List<Item> findItemByParam(String param);
}