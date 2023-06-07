package ru.practicum.shareit.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {

    /**
     * уникальный идентификатор комментария
     */
    private Long id;

    /**
     * содержимое комментария
     */
    @NotBlank
    private String text;

    /**
     * вещь, к которой относится комментарий
     */
    private Long itemId;

    /**
     * автор комментария
     */
    private Long authorId;

    /**
     * дата создания комментария
     */
    private LocalDateTime created;

    /**
     * имя автора
     */
    private String authorName;
}
