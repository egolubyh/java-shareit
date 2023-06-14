package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    private static final String FAIL_ID_MESSAGE = "Возвращаемый ID не соответствует ожидаемому";
    private static final String FAIL_OPTIONAL_MESSAGE = "Возвращаемый optional не содержит объекта";
    @Autowired
    private UserRepository repository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("testName");
        user.setEmail("test@mail.ru");
        user = repository.save(user);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testFindUserById() {
        Optional<User> result = repository.findById(user.getId());

        assertTrue(result.isPresent(), FAIL_OPTIONAL_MESSAGE);
        assertEquals(user.getId(), result.get().getId(), FAIL_ID_MESSAGE);
    }

    @Test
    void testFindUserByIdFailId() {
        Optional<User> result = repository.findById(9999L);

        assertTrue(result.isEmpty(), "Optional содержит объект");
    }
}