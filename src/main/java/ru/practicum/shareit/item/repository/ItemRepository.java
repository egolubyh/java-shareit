package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerOrderByIdAsc(User owner);

    @Query("SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(concat('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(concat('%', ?1, '%')) ) " +
            "AND i.available IS TRUE ")
    List<Item> search(String text);

    List<Item> findByItemRequest(ItemRequest itemRequest);
}