package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
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

    /**
     * запрос вещи
     */
    private Long requestId;
}
