package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerITest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        bookingDto = new BookingDto();
        bookingDto.setStart(now.plusDays(1));
        bookingDto.setEnd(now.plusDays(2));
        bookingDto.setItemId(1L);
        bookingDto.setBooker(new UserDto());
        bookingDto.setItemId(1L);
    }

    @SneakyThrows
    @Test
    void addBooking() {
        when(bookingService.addBooking(any(BookingDto.class), anyLong()))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingNullItemIdField_thenBadRequest() {
        bookingDto.setItemId(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(any(BookingDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingNullStartField_thenBadRequest() {
        bookingDto.setStart(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(any(BookingDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingNotValidStartField_thenBadRequest() {
        bookingDto.setStart(now.minusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(any(BookingDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingNullEndField_thenBadRequest() {
        bookingDto.setEnd(null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(any(BookingDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void addBooking_whenBookingNotValidEndField_thenBadRequest() {
        bookingDto.setEnd(now.minusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(any(BookingDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void getBooking() {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser() {
        List<BookingDto> expectedList = List.of(bookingDto);

        when(bookingService.getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser_whenParamFromIsLessZero_thenServerError() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "5"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser_whenParamSizeIsLessOne_thenServerError() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner() {
        List<BookingDto> expectedList = List.of(bookingDto);

        when(bookingService.getAllBookingByOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedList);

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedList), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner_whenParamFromIsLessZero_thenServerError() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "5"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner_whenParamSizeIsLessOne_thenServerError() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getAllBookingByUser(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void setApprove() {
        when(bookingService.updateApprove(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }
}