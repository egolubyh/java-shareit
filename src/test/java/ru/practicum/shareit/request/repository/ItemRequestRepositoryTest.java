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
    private static final String FAIL_SIZE_MESSAGE = "Возвращаемый список имеет размер не соответствует ожидаемому";
    private static final String FAIL_REQUESTOR_MESSAGE = "Возвращаемый requestor не соответствует ожидаемому";
    private static final String FAIL_ITEM_REQUEST_MESSAGE = "Возвращаемый itemRequest не соответствует ожидаемому";
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
    void testFindByRequestorOrderByCreatedDesc() {
        List<ItemRequest> results = requestRepository.findByRequestorOrderByCreatedDesc(user1);

        assertEquals(2, results.size(), FAIL_SIZE_MESSAGE);
        assertEquals(itemRequest1.getId(), results.get(1).getId(), FAIL_ITEM_REQUEST_MESSAGE);
        assertEquals(itemRequest2.getId(), results.get(0).getId(), FAIL_ITEM_REQUEST_MESSAGE);
    }

    @Test
    void testFindAllByRequestorNot() {
        List<ItemRequest> results = requestRepository.findAllByRequestorNot(
                user1, PageRequest.of(0,10)).getContent();

        assertEquals(1, results.size(), FAIL_SIZE_MESSAGE);
        assertEquals(itemRequest3.getId(), results.get(0).getId(), FAIL_ITEM_REQUEST_MESSAGE);
        assertEquals(user2, results.get(0).getRequestor(), FAIL_REQUESTOR_MESSAGE);
    }
}