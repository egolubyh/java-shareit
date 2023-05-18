package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemIdDto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ItemRequestDto {
    /**
     * уникальный идентификатор запроса
     */
    private Long id;

    /**
     * текст запроса, содержащий описание требуемой вещи
     */
    @NotEmpty
    private String description;

    /**
     * пользователь, создавший запрос
     */
    private Long requestorId;

    /**
     * дата создания запроса
     */
    private LocalDateTime created;

    /**
     * список вещей
     */
    private List<ItemIdDto> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDto that = (ItemRequestDto) o;
        return id.equals(that.id) && description.equals(that.description) && requestorId.equals(that.requestorId) && created.equals(that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requestorId, created);
    }
}
