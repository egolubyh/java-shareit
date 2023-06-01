package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImpTest {
    @Mock private BookingRepository bookingRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImp bookingService;
    @Captor
    ArgumentCaptor<Booking> argumentCaptor;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private LocalDateTime now;


    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        user = new User();
        user.setId(1L);

        owner = new User();
        owner.setId(2L);

        item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(now);
        booking.setEnd(now.plusDays(1));
        booking.setStatus(Status.WAITING);

        bookingDto = new BookingDto();
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(item.getId());

        lenient().when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        lenient().when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        lenient().when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        lenient().when(bookingMapper.toBooking(any(BookingDto.class), anyLong()))
                .thenReturn(booking);
        lenient().when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(bookingDto);
    }

    @Test
    void addBooking_whenAllFieldsIsOk_thenReturnBookingDto()
            throws UserNotFoundException, BookingOwnerException, DateEndBeforeStartException, NotAvailableException,
            ItemNotFoundException, DateEndEqualStartException {
        bookingService.addBooking(bookingDto, user.getId());

        verify(bookingRepository).save(argumentCaptor.capture());

        Booking actual = argumentCaptor.getValue();

        assertEquals(actual, booking);
    }

    @Test
    void addBooking_whenItemNotFound_thenItemNotFoundExceptionThrow() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void addBooking_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void addBooking_whenAvailableFalse_thenNotAvailableExceptionThrow() {
        item.setAvailable(false);

        assertThrows(NotAvailableException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void addBooking_whenDateEndBeforeStart_thenDateEndBeforeStartExceptionThrow() {
        bookingDto.setEnd(now.minusDays(1));

        assertThrows(DateEndBeforeStartException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void addBooking_whenDateEndEqualStart_thenDateEndEqualStartExceptionThrow() {
        bookingDto.setEnd(now);
        bookingDto.setStart(now);

        assertThrows(DateEndEqualStartException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void addBooking_whenBookingBadOwner_thenBookingOwnerExceptionThrow() {
        item.setOwner(user);

        assertThrows(BookingOwnerException.class,
                () -> bookingService.addBooking(bookingDto, user.getId()));
    }

    @Test
    void updateApprove_whenAllFieldsIsOk_thenSaveAndReturnBooking()
            throws BookingNotFoundException, BadBookingStatusException, BadOwnerException, UserNotFoundException {
        booking.setStatus(Status.WAITING);

        bookingService.updateApprove(booking.getId(), owner.getId(), Boolean.TRUE);

        verify(bookingRepository).save(argumentCaptor.capture());

        Booking actual = argumentCaptor.getValue();

        assertEquals(Status.APPROVED, actual.getStatus());
    }

    @Test
    void updateApprove_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingService.updateApprove(booking.getId(), user.getId(), Boolean.TRUE));
    }

    @Test
    void updateApprove_whenBookingNotFound_thenBookingNotFoundExceptionThrow() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.updateApprove(booking.getId(), owner.getId(), Boolean.TRUE));
    }

    @Test
    void updateApprove_whenBadOwner_thenBadOwnerExceptionThrow() {
        item.setOwner(owner);

        assertThrows(BadOwnerException.class,
                () -> bookingService.updateApprove(booking.getId(), user.getId(), Boolean.TRUE));
    }

    @Test
    void updateApprove_whenBadStatusBooking_thenBadBookingStatusExceptionThrow() {
        booking.setStatus(Status.APPROVED);

        assertThrows(BadBookingStatusException.class,
                () -> bookingService.updateApprove(booking.getId(), owner.getId(), Boolean.TRUE));
    }

    @Test
    void getBooking_whenAllFieldsIsOk_thenReturnBooking() throws UserNotFoundException, BookingNotFoundException {
        when(bookingRepository.findBookingByIdAndUser(anyLong(),any(User.class)))
                .thenReturn(Optional.of(booking));
        BookingDto actual = bookingService.getBooking(booking.getId(), user.getId());

        assertEquals(actual, bookingDto);
    }

    @Test
    void getBooking_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBooking(booking.getId(), user.getId()));
    }

    @Test
    void getBooking_whenBookingNotFound_thenBookingNotFoundExceptionThrow() {
        when(bookingRepository.findBookingByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBooking(booking.getId(), user.getId()));
    }

    @Test
    void getAllBookingByUser_whenStateIsALL_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByBooker(any(User.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByUser(
                user.getId(), "ALL", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsCURRENT_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByBookerAndStartBeforeAndEndAfter(
                any(User.class), any(LocalDateTime.class),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByUser(
                user.getId(), "CURRENT", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsPAST_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByBookerAndEndBefore(
                any(User.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByUser(
                user.getId(), "PAST", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsFUTURE_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByBookerAndStartGreaterThan(
                any(User.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByUser(
                user.getId(), "FUTURE", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsWAITING_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByBookerAndStatus(
                any(User.class), any(Status.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByUser(
                user.getId(), "WAITING", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsREJECTED_thenReturnBookings()
         throws UserNotFoundException, UnsupportedStatusException {
            when(bookingRepository.findBookingByBookerAndStatus(
                    any(User.class), any(Status.class), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(booking)));

            List<BookingDto> actual = bookingService.getAllBookingByUser(
                    user.getId(), "REJECTED", 0, 1);

            assertEquals(1, actual.size());
            assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByUser_whenStateIsUNKNOW_thenUnsupportedStatusExceptionThrow() {
        assertThrows(UnsupportedStatusException.class,
                () -> bookingService.getAllBookingByUser(user.getId(), "UNSUPPORTED", 0, 1));
    }

    @Test
    void getAllBookingByUser_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingByUser(user.getId(), "ALL", 0, 1));
    }

    @Test
    void getAllBookingByOwner_whenStateIsALL_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_Owner(any(User.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "ALL", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenStateIsCURRENT_thenReturnBookings() throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_OwnerAndStartBeforeAndEndAfter(
                any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "CURRENT", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenStateIsPAST_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_OwnerAndEndBefore(
                any(User.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "PAST", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenStateIsFUTURE_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_OwnerAndStartGreaterThan(
                any(User.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "FUTURE", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenStateIsWAITING_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_OwnerAndStatus(
                any(User.class), any(Status.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "WAITING", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenStateIsREJECTED_thenReturnBookings()
            throws UserNotFoundException, UnsupportedStatusException {
        when(bookingRepository.findBookingByItem_OwnerAndStatus(
                any(User.class), any(Status.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDto> actual = bookingService.getAllBookingByOwner(
                owner.getId(), "REJECTED", 0, 1);

        assertEquals(1, actual.size());
        assertEquals(bookingDto, actual.get(0));
    }

    @Test
    void getAllBookingByOwner_whenUserNotFound_thenUserNotFoundExceptionThrow() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingByUser(user.getId(), "ALL", 0, 1));
    }

    @Test
    void getAllBookingByOwner_whenUnsupportedStatus_thenUnsupportedStatusExceptionThrow() {
        assertThrows(UnsupportedStatusException.class,
                () -> bookingService.getAllBookingByOwner(user.getId(), "UNSUPPORTED", 0, 1));
    }
}