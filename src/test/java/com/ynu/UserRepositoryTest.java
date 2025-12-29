package com.ynu;

import com.ynu.entity.User;
import com.ynu.repository.UserRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Resource
    private  UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("123456");
        user.setEmail("1@example.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("test_user", savedUser.getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("123456");
        userRepository.save(user);

        User foundUser = userRepository.findByUsername("test_user");

        assertNotNull(foundUser);
        assertEquals("test_user", foundUser.getUsername());
    }

    @Test
    public void testExistsByUsername() {
        User user = new User();
        user.setUsername("exist_user");
        user.setPassword("123456");
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("exist_user");
        boolean notExists = userRepository.existsByUsername("non_exist_user");

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    public void testExistsByEmail() {
        User user = new User();
        user.setUsername("email_user");
        user.setPassword("123456");
        user.setEmail("1@example.com");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("1@example.com");

        assertTrue(exists);
    }
}
