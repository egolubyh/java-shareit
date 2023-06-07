package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class BookingIdDto {
    private Long id;
    private Long bookerId;
}
