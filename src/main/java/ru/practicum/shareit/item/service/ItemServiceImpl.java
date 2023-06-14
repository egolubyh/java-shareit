package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIdDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addNewItem(ItemDto itemDto, Long ownerId) throws UserNotFoundException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + ownerId + " не существует."));
        Item item = itemMapper.toItem(itemDto);

        item.setOwner(owner);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public CommentDto addNewComment(CommentDto commentDto, Long itemId, Long userId)
            throws ItemNotFoundException, UserNotFoundException, BookingNotFoundException {
         Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещи с id = " + itemId + " не существует."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + userId + " не существует"));
        bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(user, item, Status.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));

        commentDto.setItemId(itemId);
        commentDto.setAuthorId(userId);
        commentDto.setCreated(LocalDateTime.now());

        Comment comment = commentMapper.toComment(commentDto);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemIdDto readItem(Long itemId, Long userId) throws ItemNotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещи с id = " + itemId + " не существует."));
        Booking lastBooking = bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                item,LocalDateTime.now(),Status.APPROVED)
                .orElse(null);
        Booking nextBooking = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                item, LocalDateTime.now(), Status.APPROVED)
                .orElse(null);

        ItemIdDto itemIdDto = itemMapper.toItemIdDto(item);

        if (item.getOwner().getId().equals(userId)) {
            itemIdDto.setLastBooking(bookingMapper.toBookingIdDto(lastBooking));
            itemIdDto.setNextBooking(bookingMapper.toBookingIdDto(nextBooking));
        }

        return itemIdDto;
    }

    @Override
    public List<ItemIdDto> readAllItemByOwner(Long ownerId) throws UserNotFoundException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id = " + ownerId + " не существует"));

        return itemRepository.findByOwnerOrderByIdAsc(owner).stream()
                .map(item -> {
                    Booking lastBooking = bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                            item,LocalDateTime.now(),Status.APPROVED)
                            .orElse(null);
                    Booking nextBooking = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                            item, LocalDateTime.now(), Status.APPROVED)
                            .orElse(null);

                    ItemIdDto itemIdDto = itemMapper.toItemIdDto(item);

                    itemIdDto.setLastBooking(bookingMapper.toBookingIdDto(lastBooking));
                    itemIdDto.setNextBooking(bookingMapper.toBookingIdDto(nextBooking));

                    return itemIdDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> readAllItemByParam(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId)
            throws BadOwnerException, ItemNotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещи с id = " + itemId + " не существует."));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new BadOwnerException("Не верный владелец веши");
        }
        itemMapper.updateItem(itemDto, item);

        return itemMapper.toItemDto(itemRepository.save(item));
    }
}
