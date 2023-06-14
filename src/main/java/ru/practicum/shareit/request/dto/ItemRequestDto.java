package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemIdDto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
}
