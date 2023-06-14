package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingIdDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemIdDto {

    /**
     * уникальный идентификатор вещи
     */
    private Long id;

    /**
     * краткое название
     */
    private String name;

    /**
     * развёрнутое описание
     */
    private String description;

    /**
     * статус о том, доступна или нет вещь для аренды
     */

    private Boolean available;

    /**
     * предыдущее бронирование
     */
    private BookingIdDto lastBooking;

    /**
     * след бронирование
     */
    private BookingIdDto nextBooking;

    /**
     * комментарии
     */
    private List<CommentDto> comments;

    /**
     * ид запроса
     */
    private Long requestId;
}
