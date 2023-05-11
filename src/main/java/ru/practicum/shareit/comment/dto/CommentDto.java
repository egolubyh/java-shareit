package ru.practicum.shareit.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return id.equals(that.id) && text.equals(that.text) && itemId.equals(that.itemId) && authorId.equals(that.authorId) && created.equals(that.created) && authorName.equals(that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, itemId, authorId, created, authorName);
    }

    /**
     * имя автора
     */
    private String authorName;
}
