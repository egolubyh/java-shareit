package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) throws UserNotFoundException {
        ItemRequest itemRequest = new ItemRequest();
        Long userId = itemRequestDto.getRequestorId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));

        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(itemRequestDto.getCreated());

        return itemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestorId(itemRequest.getRequestor().getId());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(itemRepository.findByItemRequest(itemRequest).stream()
                .map(itemMapper::toItemIdDto)
                .collect(Collectors.toList()));

        return itemRequestDto;
    }
}
