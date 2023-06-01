package ru.practicum.shareit.comment.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Item item1;
    private Comment comment1;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@mail.ru");

        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@mail.ru");

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        item1 = new Item();
        item1.setName("item1");
        item1.setDescription("desc");
        item1.setAvailable(true);
        item1.setOwner(user1);

        item1 = itemRepository.save(item1);

        comment1 = new Comment();
        comment1.setText("text");
        comment1.setItem(item1);
        comment1.setAuthor(user2);
        comment1.setCreated(LocalDateTime.now());

        comment1 = commentRepository.save(comment1);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByItem() {
        List<Comment> result = commentRepository.findByItem(item1);

        assertEquals(1, result.size());
        assertEquals(comment1, result.get(0));
    }
}