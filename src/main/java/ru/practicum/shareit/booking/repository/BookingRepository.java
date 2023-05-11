package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByBookerOrderByStartDesc(User booker);

    List<Booking> findBookingByBookerAndStatusOrderByStartDesc(User booker, Status status);

    List<Booking> findBookingByBookerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime start);

    List<Booking> findBookingByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime end);

    List<Booking> findBookingByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingByItem_OwnerAndStatusOrderByStartDesc(User itemOwner, Status status);

    List<Booking> findBookingByItem_OwnerOrderByStartDesc(User itemOwner);

    List<Booking> findBookingByItem_OwnerAndStartGreaterThanOrderByStartDesc(User itemOwner, LocalDateTime start);

    List<Booking> findBookingByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(User itemOwner, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingByItem_OwnerAndEndBeforeOrderByStartDesc(User itemOwner, LocalDateTime start);

    @Query ("SELECT b FROM Booking b " +
            "WHERE b.id = ?1 AND (b.booker = ?2 OR b.item.owner = ?2) ")
    Optional<Booking> findBookingByIdAndUser(Long id, User user);

    Optional<Booking> findFirstByBookerAndItemAndStatusAndEndBefore(User booker, Item item, Status status, LocalDateTime end);

    Optional<Booking> findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusOrderByStartAsc(Item item, LocalDateTime start, Status status);
}
