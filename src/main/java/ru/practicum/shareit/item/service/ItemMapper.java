package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepository;

    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        ItemRequest itemRequest = item.getItemRequest();
        Long requestId = itemRequest != null ? itemRequest.getId() : null;

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(requestId);

        return itemDto;
    }

    public ItemIdDto toItemIdDto(Item item) {
        ItemIdDto itemIdDto = new ItemIdDto();
        ItemRequest itemRequest = item.getItemRequest();

        itemIdDto.setId(item.getId());
        itemIdDto.setName(item.getName());
        itemIdDto.setDescription(item.getDescription());
        itemIdDto.setAvailable(item.getAvailable());
        itemIdDto.setRequestId(itemRequest != null ? itemRequest.getId() : null);
        itemIdDto.setComments(commentRepository.findByItem(item).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return itemIdDto;
    }

    public Item toItem(ItemDto itemDto) {
        Item item = new Item();
        Long requestId = itemDto.getRequestId();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        if (requestId != null) {
            item.setItemRequest(itemRequestRepository.findById(requestId)
                    .orElse(null));
        }
        return item;
    }

    public void updateItem(ItemDto itemDto, Item item) {
        String nameItemDto = itemDto.getName();
        String descriptionItemDto = itemDto.getDescription();
        Boolean availableItemDto = itemDto.getAvailable();

        if (nameItemDto != null) {
            item.setName(nameItemDto);
        }
        if (descriptionItemDto != null) {
            item.setDescription(descriptionItemDto);
        }
        if (availableItemDto != null) {
            item.setAvailable(availableItemDto);
        }
    }
}