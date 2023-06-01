package ru.practicum.shareit.booking.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setBooker(new UserDto());
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now.plusDays(2));
    }

    @SneakyThrows
    @Test
    void addBooking() {
        when(bookingService.addBooking(any(BookingDto.class), anyLong()))
                .thenReturn(bookingDto);

        BookingDto actual = bookingController.addBooking(bookingDto, 1L);

        assertEquals(bookingDto, actual);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        BookingDto actual = bookingController.getBooking(anyLong(),anyLong());

        assertEquals(bookingDto, actual);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser() {
        List<BookingDto> expectedList = List.of(bookingDto);
        when(bookingService.getAllBookingByUser(anyLong(), anyString(),anyInt(),anyInt()))
                .thenReturn(expectedList);

        List<BookingDto> actual = bookingController.getAllBookingByUser(anyLong(), anyString(),anyInt(),anyInt());

        assertEquals(expectedList, actual);
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner() {
        List<BookingDto> expectedList = List.of(bookingDto);
        when(bookingService.getAllBookingByOwner(anyLong(), anyString(),anyInt(),anyInt()))
                .thenReturn(expectedList);

        List<BookingDto> actual = bookingController.getAllBookingByOwner(anyLong(), anyString(),anyInt(),anyInt());

        assertEquals(expectedList, actual);
    }

    @SneakyThrows
    @Test
    void setApprove() {
        when(bookingService.updateApprove(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        BookingDto actual = bookingController.setApprove(anyLong(), anyLong(), anyBoolean());

        assertEquals(bookingDto, actual);
    }
}