package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    /**
     * уникальный идентификатор бронирования
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * дата и время начала бронирования
     */
    @Column(name = "start_date")
    private LocalDateTime start;

    /**
     * дата и время конца бронирования
     */
    @Column(name = "end_date")
    private LocalDateTime end;

    /**
     * вещь, которую пользователь бронирует
     */
    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    /**
     * пользователь, который осуществляет бронирование
     */
    @OneToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;

    /**
     * статус бронирования
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;
}
