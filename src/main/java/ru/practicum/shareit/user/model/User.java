package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
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
}
