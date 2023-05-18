package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {

    /**
     * уникальный идентификатор запроса
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * текст запроса, содержащий описание требуемой вещи
     */
    @Column(name = "description")
    private String description;

    /**
     * пользователь, создавший запрос
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id", referencedColumnName = "id")
    private User requestor;

    /**
     * дата создания запроса
     */
    @Column(name = "created")
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return id.equals(that.id) && description.equals(that.description) && requestor.equals(that.requestor) && created.equals(that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requestor, created);
    }
}
