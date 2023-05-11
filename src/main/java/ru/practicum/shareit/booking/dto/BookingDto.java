package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return id.equals(that.id) && itemId.equals(that.itemId) && start.equals(that.start) && end.equals(that.end) && booker.equals(that.booker) && status == that.status && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, start, end, booker, status, item);
    }
}
