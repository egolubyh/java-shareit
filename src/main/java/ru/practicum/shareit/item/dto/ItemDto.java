package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ItemDto {

    /**
     * уникальный идентификатор вещи
     */
    private Long id;

    /**
     * краткое название
     */
    @NotBlank
    private String name;

    /**
     * развёрнутое описание
     */
    @NotBlank
    private String description;

    /**
     * статус о том, доступна или нет вещь для аренды
     */
    @NotNull
    private Boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return id.equals(itemDto.id) && name.equals(itemDto.name) && description.equals(itemDto.description)
                && available.equals(itemDto.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available);
    }
}
