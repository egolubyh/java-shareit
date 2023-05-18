package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") Long userId)
            throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /requests, метод POST");

        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findItemRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId)
            throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /requests, метод GET");

        return itemRequestService.readItemRequestByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable(value = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        log.info("Получен запрос к эндпоинту: /requests/{}, метод GET", requestId);

        return itemRequestService.readItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "from", defaultValue = "0", required = false) @Min (0) Integer from,
            @RequestParam(value = "size", defaultValue = "20", required = false) @Min (1) Integer size)
            throws UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /requests/all, метод GET");

        return itemRequestService.readAll(userId, from, size);
    }
}
