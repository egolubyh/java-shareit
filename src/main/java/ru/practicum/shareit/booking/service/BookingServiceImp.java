package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.BadOwnerException;
import ru.practicum.shareit.item.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto addBooking(BookingDto bookingDto, Long userId)
            throws ItemNotFoundException, NotAvailableException, UserNotFoundException, DateEndBeforeStartException,
            DateEndEqualStartException, BookingOwnerException {
        validDateFields(bookingDto);
        long itemId = bookingDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item с id = " + itemId + " не существует."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с id = " + userId + " не существует."));

        if (!item.getAvailable()) {
            throw new NotAvailableException("Item недоступна для бронирования");
        }

        if (item.getOwner().equals(user)) {
            throw new BookingOwnerException("Владелец не может бронировать свою вещь");
        }
        Booking bookings = bookingRepository.save(bookingMapper.toBooking(bookingDto, userId));

        return bookingMapper.toBookingDto(bookings);
    }

    @Override
    @Transactional
    public BookingDto updateApprove(Long bookingId, Long userId, Boolean approved)
            throws UserNotFoundException, BookingNotFoundException, BadOwnerException, BadBookingStatusException {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с id = " + userId + " не существует."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking с id = " + bookingId + " не существует."));
        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            throw new BadOwnerException("Не верный владелец веши");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BadBookingStatusException("Статус уже подтвержден");
        }
        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) throws UserNotFoundException, BookingNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с id = " + userId + " не существует."));
        Booking booking = bookingRepository.findBookingByIdAndUser(bookingId, user)
                .orElseThrow(() -> new BookingNotFoundException("Booking с id = " + bookingId + " не существует."));

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingByUser(Long userId, String state)
            throws UserNotFoundException, UnsupportedStatusException {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с id = " + userId + " не существует."));
        List<Booking> bookings;

        switch (state) {
            case "ALL" : {
                bookings = bookingRepository.findBookingByBookerOrderByStartDesc(booker);
                break;
            } case "CURRENT" : {
                bookings = bookingRepository.findBookingByBookerAndStartBeforeAndEndAfterOrderByStartDesc(booker,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            } case "PAST" : {
                bookings = bookingRepository.findBookingByBookerAndEndBeforeOrderByStartDesc(
                        booker, LocalDateTime.now());
                break;
            } case "FUTURE" : {
                bookings = bookingRepository.findBookingByBookerAndStartGreaterThanOrderByStartDesc(booker,
                        LocalDateTime.now());
                break;
            } case "WAITING" : {
                bookings = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(booker, Status.WAITING);
                break;
            } case "REJECTED" : {
                bookings = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(booker, Status.REJECTED);
                break;
            } default: {
                throw new UnsupportedStatusException("Unknown state: " + state);
            }
        }

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingByOwner(Long ownerId, String state)
            throws UserNotFoundException, UnsupportedStatusException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User с id = " + ownerId + " не существует."));
        List<Booking> bookings;

        switch (state) {
            case "ALL" : {
                bookings = bookingRepository.findBookingByItem_OwnerOrderByStartDesc(owner);
                break;
            } case "CURRENT" : {
                bookings = bookingRepository.findBookingByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(owner,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            } case "PAST" : {
                bookings = bookingRepository.findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(
                        owner, LocalDateTime.now());
                break;
            } case "FUTURE" : {
                bookings = bookingRepository.findBookingByItem_OwnerAndStartGreaterThanOrderByStartDesc(owner,
                        LocalDateTime.now());
                break;
            } case "WAITING" : {
                bookings = bookingRepository.findBookingByItem_OwnerAndStatusOrderByStartDesc(owner, Status.WAITING);
                break;
            } case "REJECTED" : {
                bookings = bookingRepository.findBookingByItem_OwnerAndStatusOrderByStartDesc(owner, Status.REJECTED);
                break;
            } default: {
                throw new UnsupportedStatusException("Unknown state: " + state);
            }
        }

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void validDateFields(BookingDto booking) throws DateEndBeforeStartException, DateEndEqualStartException {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();

        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new DateEndBeforeStartException("Дата начала бронирования не может быть " +
                        "после даты окончания бронирования.");
            }
            if (end.isEqual(start)) {
                throw new DateEndEqualStartException("Дата начала бронирования не может быть " +
                        "равна дате окончания бронирования.");
            }
        }
    }
}
