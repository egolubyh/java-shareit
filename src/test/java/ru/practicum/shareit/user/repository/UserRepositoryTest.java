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
    void findUserById() {
        Optional<User> result = repository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    void findUserByIdFailId() {
        Optional<User> result = repository.findById(9999L);

        assertTrue(result.isEmpty());
    }
}