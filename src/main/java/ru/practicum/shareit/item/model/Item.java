package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    /**
     * уникальный идентификатор вещи
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * краткое название
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * развёрнутое описание
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * статус о том, доступна или нет вещь для аренды
     */
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    /**
     * владелец вещи
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    /**
     * если вещь создана по запросу другого пользователя, тут будет ссылка на запрос
     */
    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest itemRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return getId() != null && Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
