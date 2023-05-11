package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserMapper;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        ItemDto itemDto = itemMapper.toItemDto(booking.getItem());
        UserDto userDto = userMapper.toUserDto(booking.getBooker());

        bookingDto.setId(booking.getId());
        bookingDto.setItemId(itemDto.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(userDto);
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public Booking toBooking(BookingDto bookingDto, Long ownerId) {
        ru.practicum.shareit.booking.model.Booking booking = new ru.practicum.shareit.booking.model.Booking();
        Item item = itemRepository.findById(bookingDto.getItemId()).orElse(null);
        User user = userRepository.findById(ownerId).orElse(null);

        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(user);
        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }

        return booking;
    }

    public BookingIdDto toBookingIdDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingIdDto bookingIdDto = new BookingIdDto();

        bookingIdDto.setId(booking.getId());
        bookingIdDto.setBookerId(booking.getBooker().getId());

        return bookingIdDto;
    }
}
