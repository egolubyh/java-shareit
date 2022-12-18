package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    /**
     * уникальный идентификатор бронирования
     */
    private Long id;
    /**
     * дата и время начала бронирования
     */
    private LocalDateTime start;
    /**
     * дата и время конца бронирования
     */
    private LocalDateTime end;
    /**
     * вещь, которую пользователь бронирует
     */
    private Item item;
    /**
     * пользователь, который осуществляет бронирование
     */
    private User booker;
    /**
     * статус бронирования
     */
    private Status status;
}
