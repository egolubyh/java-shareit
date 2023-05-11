package ru.practicum.shareit.user.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * имя или логин пользователя
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * адрес электронной почты
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * список вещей, которыми владеет пользователь
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
