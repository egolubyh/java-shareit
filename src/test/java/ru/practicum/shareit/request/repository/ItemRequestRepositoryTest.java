package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@mail.ru");

        user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        itemRequest1 = new ItemRequest();
        itemRequest1.setDescription("desc");
        itemRequest1.setRequestor(user1);
        itemRequest1.setCreated(now.plusDays(1));

        itemRequest2 = new ItemRequest();
        itemRequest2.setDescription("desc");
        itemRequest2.setRequestor(user1);
        itemRequest2.setCreated(now.plusDays(2));

        itemRequest3 = new ItemRequest();
        itemRequest3.setDescription("desc");
        itemRequest3.setRequestor(user2);
        itemRequest3.setCreated(now.plusDays(3));

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        itemRequest1 = requestRepository.save(itemRequest1);
        itemRequest2 = requestRepository.save(itemRequest2);
        itemRequest3 = requestRepository.save(itemRequest3);
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByRequestorOrderByCreatedDesc() {
        List<ItemRequest> results = requestRepository.findByRequestorOrderByCreatedDesc(user1);

        assertEquals(2, results.size());
        assertEquals(itemRequest1.getId(), results.get(1).getId());
        assertEquals(itemRequest2.getId(), results.get(0).getId());
    }

    @Test
    void findAllByRequestorNot() {
        List<ItemRequest> results = requestRepository.findAllByRequestorNot(
                user1, PageRequest.of(0,10)).getContent();

        assertEquals(1, results.size());
        assertEquals(itemRequest3.getId(), results.get(0).getId());
        assertEquals(user2, results.get(0).getRequestor());
    }
}