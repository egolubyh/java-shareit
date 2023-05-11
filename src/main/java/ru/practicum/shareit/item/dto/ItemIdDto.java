package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingIdDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
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
     * владелец вещи
     */
    private Long owner;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemIdDto itemIdDto = (ItemIdDto) o;
        return id.equals(itemIdDto.id) && name.equals(itemIdDto.name) && description.equals(itemIdDto.description) && available.equals(itemIdDto.available) && owner.equals(itemIdDto.owner) && lastBooking.equals(itemIdDto.lastBooking) && nextBooking.equals(itemIdDto.nextBooking) && comments.equals(itemIdDto.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, owner, lastBooking, nextBooking, comments);
    }
}
