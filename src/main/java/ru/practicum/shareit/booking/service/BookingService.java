package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.BadOwnerException;
import ru.practicum.shareit.item.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto booking, Long id)
            throws ItemNotFoundException, NotAvailableException, UserNotFoundException,
            DateEndBeforeStartException, DateEndEqualStartException, BookingOwnerException;

    BookingDto updateApprove(Long bookingId, Long userId, Boolean approved)
            throws UserNotFoundException, BookingNotFoundException, BadOwnerException,
            BadBookingStatusException;

    BookingDto getBooking(Long bookingId, Long userId)
            throws UserNotFoundException, BookingNotFoundException;

    List<BookingDto> getAllBookingByUser(Long userId, String state)
            throws UserNotFoundException, UnsupportedStatusException;

    List<BookingDto> getAllBookingByOwner(Long ownerId, String state)
            throws UserNotFoundException, UnsupportedStatusException;
}
