package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final UserRepository userRepository;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? item.getOwner().getId() : null)
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner() != null ? userRepository.findUserById(itemDto.getOwner()) : null)
                .build();
    }

    public void updateItem(ItemDto itemDto, Item item) {
        String nameItemDto = itemDto.getName();
        String descriptionItemDto = itemDto.getDescription();
        Boolean availableItemDto = itemDto.getAvailable();

        if (nameItemDto != null) item.setName(nameItemDto);
        if (descriptionItemDto != null) item.setDescription(descriptionItemDto);
        if (availableItemDto != null) item.setAvailable(availableItemDto);
    }
}
