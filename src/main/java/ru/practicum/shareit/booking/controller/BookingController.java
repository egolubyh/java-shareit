package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.BadOwnerException;
import ru.practicum.shareit.item.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestBody @Valid BookingDto booking,
                              @RequestHeader("X-Sharer-User-Id") Long userId)
            throws ItemNotFoundException, NotAvailableException, UserNotFoundException,
            DateEndBeforeStartException, DateEndEqualStartException, BookingOwnerException {
        log.info("Получен запрос к эндпоинту: /bookings, метод POST");

        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApprove(@PathVariable(value = "bookingId") Long bookingId,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @RequestParam("approved") Boolean approved)
            throws UserNotFoundException, BookingNotFoundException, BadOwnerException, BadBookingStatusException {
        log.info("Получен запрос к эндпоинту: /bookings/{}, метод PATCH", bookingId);

        return bookingService.updateApprove(bookingId,userId,approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable(value = "bookingId") Long bookingId,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId)
            throws BookingNotFoundException, UserNotFoundException {
        log.info("Получен запрос к эндпоинту: /bookings/{}, метод GET", bookingId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingByUser(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                          @RequestParam(value = "state", defaultValue = "ALL",
                                                  required = false) String state)
            throws UserNotFoundException, UnsupportedStatusException {
        log.info("Получен запрос к эндпоинту: /bookings, метод GET");
        return bookingService.getAllBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                          @RequestParam(value = "state", defaultValue = "ALL",
                                                  required = false) String state)
            throws UserNotFoundException, UnsupportedStatusException {
        log.info("Получен запрос к эндпоинту: /bookings, метод GET");
        return bookingService.getAllBookingByOwner(ownerId, state);
    }
}
