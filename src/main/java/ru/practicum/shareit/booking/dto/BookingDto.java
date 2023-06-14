package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingDto {
    /**
     * уникальный идентификатор бронирования
     */
    private Long id;

    /**
     * вещь, которую пользователь бронирует
     */
    @NotNull
    private Long itemId;

    /**
     * дата и время начала бронирования
     */
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    /**
     * дата и время конца бронирования
     */
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;

    /**
     * пользователь, который осуществляет бронирование
     */
    private UserDto booker;

    /**
     * статус бронирования
     */
    private Status status;

    private ItemDto item;
}
