package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
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
     * если вещь была создана по запросу другого пользователя, то в этом
     * поле будет храниться ссылка на соответствующий запрос.
     */
    private Long request;
}
