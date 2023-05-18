package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImp implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) throws UserNotFoundException {
        itemRequestDto.setRequestorId(userId);
        itemRequestDto.setCreated(LocalDateTime.now());

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> readItemRequestByUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));

        return itemRequestRepository.findByRequestorOrderByCreatedDesc(user).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto readItemRequestById(Long userId, Long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запроса с id = " + requestId + " не существует"));

        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> readAll(Long userId, Integer from, Integer size) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        int page = from > 0 ? from / size : 0;

        return itemRequestRepository.findAllByRequestorNot(user,
                        PageRequest.of(page, size, Sort.by("created").descending()))
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}