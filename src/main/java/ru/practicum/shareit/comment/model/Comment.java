package ru.practicum.shareit.comment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * уникальный идентификатор комментария
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * содержимое комментария
     */
    @Column(name = "post_text")
    private String text;

    /**
     * вещь, к которой относится комментарий
     */
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    /**
     * автор комментария
     */
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    /**
     * дата создания комментария
     */
    @Column(name = "created")
    private LocalDateTime created;
}
