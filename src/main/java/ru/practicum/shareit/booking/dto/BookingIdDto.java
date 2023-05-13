package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookingIdDto {
    private Long id;
    private Long bookerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingIdDto that = (BookingIdDto) o;
        return id.equals(that.id) && bookerId.equals(that.bookerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookerId);
    }
}
