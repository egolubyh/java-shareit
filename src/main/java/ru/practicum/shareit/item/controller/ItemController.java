package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /items, метод POST");
        return itemService.addNewItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody @Valid CommentDto commentDto,
                                 @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId)
            throws UserNotFoundException, ItemNotFoundException, BookingNotFoundException {
        log.info("Получен запрос к эндпоинту: items/{}/comment, метод POST", itemId);
        return itemService.addNewComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws BadOwnerException, ItemNotFoundException {
        log.info("Получен запрос к эндпоинту: /items/{}, метод PATCH", itemId);
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemIdDto findItemById(@PathVariable Long itemId,
                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) throws ItemNotFoundException {
        log.info("Получен запрос к эндпоинту: /items/{}, метод GET", itemId);
        return itemService.readItem(itemId, ownerId);
    }

    @GetMapping
    public List<ItemIdDto> findItemByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /items, метод GET");
        return itemService.readAllItemByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByParam(@RequestParam("text") String text) {
        log.info("Получен запрос к эндпоинту: /items/search?text={}, метод GET", text);
        return itemService.readAllItemByParam(text);
    }


}
