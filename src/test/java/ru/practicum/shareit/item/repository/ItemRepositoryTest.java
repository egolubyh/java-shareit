package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user1;
    private Item item1;
    private Item item2;
    private Item item3;
    private ItemRequest itemRequest1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@mail.ru");

        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        item1 = new Item();
        item1.setName("text_item1");
        item1.setDescription("desc");
        item1.setAvailable(true);
        item1.setOwner(user1);

        item2 = new Item();
        item2.setName("item2");
        item2.setDescription("text_desc");
        item2.setAvailable(false);
        item2.setOwner(user1);

        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);

        itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("desc");
        itemRequest1.setRequestor(user2);
        itemRequest1.setCreated(LocalDateTime.now());

        itemRequest1 = itemRequestRepository.save(itemRequest1);

        item3 = new Item();
        item3.setName("item3");
        item3.setDescription("Text_desc");
        item3.setAvailable(true);
        item3.setOwner(user1);
        item3.setItemRequest(itemRequest1);

        item3 = itemRepository.save(item3);
    }

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByOwnerOrderByIdAsc() {
        List<Item> result = itemRepository.findByOwnerOrderByIdAsc(user1);

        assertEquals(3, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item2, result.get(1));
        assertEquals(item3, result.get(2));
        assertEquals(itemRequest1, result.get(2).getItemRequest());
    }

    @Test
    void search() {
        List<Item> result = itemRepository.search("TeXt");

        assertEquals(2, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item3, result.get(1));
    }

    @Test
    void findByItemRequest() {
        List<Item> result = itemRepository.findByItemRequest(itemRequest1);

        assertEquals(1, result.size());
        assertEquals(item3, result.get(0));
    }
}