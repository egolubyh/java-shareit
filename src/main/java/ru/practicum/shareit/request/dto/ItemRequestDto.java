package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemRequestDto {
    /**
     * уникальный идентификатор запроса
     */
    private Long id;

    /**
     * текст запроса, содержащий описание требуемой вещи
     */
    private String description;

    /**
     * пользователь, создавший запрос
     */
    private Long requestorId;

}
