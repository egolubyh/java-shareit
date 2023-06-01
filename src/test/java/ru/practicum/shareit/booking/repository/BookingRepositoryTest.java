package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;


    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@mail.ru");

        user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("desc");
        item1.setAvailable(true);
        item1.setOwner(user1);

        item2 = new Item();
        item2.setName("item2");
        item2.setDescription("desc");
        item2.setAvailable(true);
        item2.setOwner(user2);

        item3 = new Item();
        item3.setName("item3");
        item3.setDescription("desc");
        item3.setAvailable(true);
        item3.setOwner(user1);

        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        item3 = itemRepository.save(item3);

        booking1 = new Booking();
        booking1.setItem(item1);
        booking1.setBooker(user2);
        booking1.setStatus(Status.WAITING);
        booking1.setStart(NOW);
        booking1.setEnd(NOW.plusDays(1));

        booking2 = new Booking();
        booking2.setItem(item2);
        booking2.setBooker(user1);
        booking2.setStatus(Status.APPROVED);
        booking2.setStart(NOW);
        booking2.setEnd(NOW.plusDays(1));

        booking3 = new Booking();
        booking3.setItem(item3);
        booking3.setBooker(user1);
        booking3.setStatus(Status.APPROVED);
        booking3.setStart(NOW.plusDays(3));
        booking3.setEnd(NOW.plusDays(4));

        booking4 = new Booking();
        booking4.setItem(item3);
        booking4.setBooker(user1);
        booking4.setStatus(Status.APPROVED);
        booking4.setStart(NOW.plusDays(12));
        booking4.setEnd(NOW.plusDays(14));

        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);
        booking4 = bookingRepository.save(booking4);
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findBookingByBooker() {
        List<Booking> result = bookingRepository.findBookingByBooker(
                user2, PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking1, result.get(0));
    }

    @Test
    void findBookingByBookerAndStatus() {
        List<Booking> result = bookingRepository.findBookingByBookerAndStatus(
                user1, Status.APPROVED, PAGE_REQUEST).getContent();

        assertEquals(3, result.size());
        assertEquals(booking2, result.get(0));
        assertEquals(booking3, result.get(1));
        assertEquals(booking4, result.get(2));
    }

    @Test
    void findBookingByBookerAndStartGreaterThan() {
        List<Booking> result = bookingRepository.findBookingByBookerAndStartGreaterThan(
                user1, NOW.plusDays(10), PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking4, result.get(0));
    }

    @Test
    void findBookingByBookerAndEndBefore() {
        List<Booking> result = bookingRepository.findBookingByBookerAndEndBefore(
                user1, NOW.plusDays(2), PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking2, result.get(0));
    }

    @Test
    void findBookingByBookerAndStartBeforeAndEndAfter() {
        List<Booking> result = bookingRepository.findBookingByBookerAndStartBeforeAndEndAfter(
                user1, NOW.plusHours(12), NOW.plusHours(12), PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking2, result.get(0));
     }

    @Test
    void findBookingByItem_OwnerAndStatus() {
        List<Booking> result = bookingRepository.findBookingByItem_OwnerAndStatus(
                user2, Status.APPROVED, PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking2, result.get(0));
    }

    @Test
    void findBookingByItem_Owner() {
        List<Booking> result = bookingRepository.findBookingByItem_Owner(
                user1, PAGE_REQUEST).getContent();

        assertEquals(3, result.size());
        assertEquals(booking1, result.get(0));
        assertEquals(booking3, result.get(1));
        assertEquals(booking4, result.get(2));
     }

    @Test
    void findBookingByItem_OwnerAndStartGreaterThan() {
        List<Booking> result = bookingRepository.findBookingByItem_OwnerAndStartGreaterThan(
                user1,NOW.plusDays(1), PAGE_REQUEST).getContent();

        assertEquals(2, result.size());
        assertEquals(booking3, result.get(0));
        assertEquals(booking4, result.get(1));
    }

    @Test
    void findBookingByItem_OwnerAndStartBeforeAndEndAfter() {
        List<Booking> result = bookingRepository.findBookingByItem_OwnerAndStartBeforeAndEndAfter(
                user1, NOW.plusHours(12), NOW.plusHours(12), PAGE_REQUEST).getContent();

        assertEquals(1, result.size());
        assertEquals(booking1, result.get(0));
    }

    @Test
    void findBookingByItem_OwnerAndEndBefore() {
        List<Booking> result = bookingRepository.findBookingByItem_OwnerAndEndBefore(
                user1, NOW.plusDays(5), PAGE_REQUEST).getContent();

        assertEquals(2, result.size());
        assertEquals(booking1, result.get(0));
        assertEquals(booking3, result.get(1));
    }

    @Test
    void findBookingByIdAndUser() {
        Optional<Booking> result = bookingRepository.findBookingByIdAndUser(booking2.getId(), user1);

        assertTrue(result.isPresent());
        assertEquals(booking2, result.get());
    }

    @Test
    void findFirstByBookerAndItemAndStatusAndEndBefore() {
        Optional<Booking> result = bookingRepository.findFirstByBookerAndItemAndStatusAndEndBefore(
                user1, item2, Status.APPROVED, NOW.plusDays(5));

        assertTrue(result.isPresent());
        assertEquals(booking2, result.get());
    }

    @Test
    void findFirstByItemAndStartBeforeAndStatusOrderByStartDesc() {
        Optional<Booking> result = bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                item3, NOW.plusDays(13),Status.APPROVED);

        assertTrue(result.isPresent());
        assertEquals(booking4, result.get());
    }

    @Test
    void findFirstByItemAndStartAfterAndStatusOrderByStartAsc() {
        Optional<Booking> result = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                item3, NOW, Status.APPROVED);

        assertTrue(result.isPresent());
        assertEquals(booking3, result.get());
    }
}