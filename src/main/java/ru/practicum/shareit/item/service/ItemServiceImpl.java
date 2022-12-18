package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    @Override
    public ItemDto addNewItem(ItemDto itemDto, Long ownerId) throws UserNotFoundException, NoNameException, NoAvailableException, NoDescriptionException {
        if (userRepository.findUserById(ownerId) == null) {
            throw new UserNotFoundException("Пользователя с id = " + ownerId + " не существует");
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new NoNameException("Поле name пустое");
        }
        if (itemDto.getAvailable() == null) {
            throw new NoAvailableException("Поле available пустое");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new NoDescriptionException("Поле description пустое");
        }

        itemDto.setOwner(ownerId);

        Item item = itemRepository.saveItem(itemMapper.toItem(itemDto));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto readItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findItemById(itemId);

        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> readAllItemByOwner(Long ownerId) {
        return itemRepository.findItemByOwner(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> readAllItemByParam(String search) {
        return itemRepository.findItemByParam(search).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) throws BadOwnerException {
        Item item = itemRepository.findItemById(itemId);
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new BadOwnerException("Не верный владелец веши");
        }
        itemMapper.updateItem(itemDto, item);

        return itemMapper.toItemDto(item);
    }
}
