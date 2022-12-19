package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item saveItem(Item item) {
        item.setId(++id);
        items.put(item.getId(),item);
        return item;
    }

    @Override
    public Item findItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findItemByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemByParam(String param) {

        return items.values().stream()
                .filter(item -> !param.isEmpty() && (item.getAvailable() &&
                        (item.getName().toLowerCase().contains(param.toLowerCase())
                                || item.getDescription().toLowerCase().contains(param.toLowerCase()))))
                .collect(Collectors.toList());
    }
}
