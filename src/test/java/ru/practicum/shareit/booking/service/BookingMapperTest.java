package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {
    private static final String FAIL_ID_MESSAGE = "ID не соответствует ожидаемому";
    private static final String FAIL_ITEM_MESSAGE = "Item не соответствует ожидаемому";
    private static final String FAIL_START_MESSAGE = "Дата начала не соответствует ожидаемой";
    private static final String FAIL_END_MESSAGE = "Дата конца бронирования не соответствует ожидаемой";
    private static final String FAIL_BOOKER_MESSAGE = "Booker не соответствует ожидаемому";
    private static final String FAIL_STATUS_MESSAGE = "Status не соответствует ожидаемому";
    @Mock private ItemRepository itemRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private ItemMapper itemMapper;
    @InjectMocks
    private BookingMapper bookingMapper;
    private BookingDto bookingDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(user);

        Mockito
                .lenient().when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .lenient().when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(userMapper.toUserDto(Mockito.any(User.class))).thenCallRealMethod();
        Mockito
                .when(itemMapper.toItemDto(Mockito.any(Item.class))).thenCallRealMethod();

        ItemDto itemDto = itemMapper.toItemDto(item);
        bookingDto = new BookingDto();
        bookingDto.setItem(itemDto);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now());
        bookingDto.setBooker(userMapper.toUserDto(user));

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void testToBooking() {
        Booking result = bookingMapper.toBooking(bookingDto,1L);

        assertNotNull(result);
        assertEquals(bookingDto.getItem().getId(), result.getItem().getId(), FAIL_ITEM_MESSAGE);
        assertEquals(bookingDto.getStart(), result.getStart(), FAIL_START_MESSAGE);
        assertEquals(bookingDto.getEnd(), result.getEnd(), FAIL_END_MESSAGE);
        assertEquals(bookingDto.getBooker().getId(), result.getBooker().getId(), FAIL_BOOKER_MESSAGE);
        assertEquals(Status.WAITING, result.getStatus(), FAIL_STATUS_MESSAGE);
    }

    @Test
    void testToBookingDto() {
        BookingDto result = bookingMapper.toBookingDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(booking.getItem().getId(), result.getItemId(), FAIL_ITEM_MESSAGE);
        assertEquals(booking.getStart(), result.getStart(), FAIL_START_MESSAGE);
        assertEquals(booking.getEnd(), result.getEnd(), FAIL_END_MESSAGE);
        assertEquals(booking.getItem().getId(), result.getItem().getId(), FAIL_ITEM_MESSAGE);
        assertEquals(booking.getBooker().getId(), result.getBooker().getId(), FAIL_BOOKER_MESSAGE);
        assertEquals(booking.getStatus(), result.getStatus(), FAIL_STATUS_MESSAGE);
    }

    @Test
    void testToBookingIdDto() {
        BookingIdDto result = bookingMapper.toBookingIdDto(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId(), FAIL_ID_MESSAGE);
        assertEquals(booking.getBooker().getId(), result.getBookerId(), FAIL_BOOKER_MESSAGE);
    }
}