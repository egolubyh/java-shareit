package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) throws UserNotFoundException,
            NoDescriptionException, NoNameException, NoAvailableException {
        log.info("Получен запрос к эндпоинту: /items, метод POST");
        return itemService.addNewItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) throws BadOwnerException {
        log.info("Получен запрос к эндпоинту: /items/{}, метод PATCH", itemId);
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable Long itemId,
                            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос к эндпоинту: /items/{}, метод GET", itemId);
        return itemService.readItem(itemId, ownerId);
    }

    @GetMapping()
    public List<ItemDto> findItemByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос к эндпоинту: /items, метод GET");
        return itemService.readAllItemByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByParam(@RequestParam("text") String s) {
        log.info("Получен запрос к эндпоинту: /items/search?text={}, метод GET",s);
        return itemService.readAllItemByParam(s);
    }


}
