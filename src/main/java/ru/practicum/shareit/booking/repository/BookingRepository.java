package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findBookingByBooker(
            User booker, Pageable pageable);

    Page<Booking> findBookingByBookerAndStatus(
            User booker, Status status, Pageable pageable);

    Page<Booking> findBookingByBookerAndStartGreaterThan(
            User booker, LocalDateTime start, Pageable pageable);

    Page<Booking> findBookingByBookerAndEndBefore(
            User booker, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingByBookerAndStartBeforeAndEndAfter(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingByItem_OwnerAndStatus(
            User itemOwner, Status status, Pageable pageable);

    Page<Booking> findBookingByItem_Owner(
            User itemOwner, Pageable pageable);

    Page<Booking> findBookingByItem_OwnerAndStartGreaterThan(
            User itemOwner, LocalDateTime start, Pageable pageable);

    Page<Booking> findBookingByItem_OwnerAndStartBeforeAndEndAfter(
            User itemOwner, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingByItem_OwnerAndEndBefore(
            User itemOwner, LocalDateTime start, Pageable pageable);

    @Query ("SELECT b FROM Booking b " +
            "WHERE b.id = ?1 AND (b.booker = ?2 OR b.item.owner = ?2) ")
    Optional<Booking> findBookingByIdAndUser(Long id, User user);

    Optional<Booking> findFirstByBookerAndItemAndStatusAndEndBefore(
            User booker, Item item, Status status, LocalDateTime end);

    Optional<Booking> findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
            Item item, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
            Item item, LocalDateTime start, Status status);
}
