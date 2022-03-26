package net.ancronik.cookbook.backend.authentication.integrationtest.data.repository;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.UserAuthSpringBootApp;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import net.ancronik.cookbook.backend.authentication.integrationtest.PostgresTestContainersExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({PostgresTestContainersExtension.class, SpringExtension.class})
@SpringBootTest(classes = UserAuthSpringBootApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TestTypes.INTEGRATION)
public class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Order(1)
    @Test
    public void save_TestConstraintsThatAreNotCoveredByValidation() {
        assertEquals(0, userRepository.count());

        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@email.com");


        Exception e = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertTrue(e.getMessage().contains("constraint [password_hash]"));

        assertEquals(0, userRepository.count());
    }

    @Order(2)
    @Test
    public void save_CreateSingleUser() {
        assertEquals(0, userRepository.count());

        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setPasswordHash(passwordEncoder.encode("password"));
        user.setEditor(true);


        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertTrue(savedUser.isEditor());

        assertEquals(1, userRepository.count());
    }

    @Order(3)
    @Test
    public void save_TestUniqueConstraints() {
        assertEquals(1, userRepository.count());

        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test1@email.com");
        user.setPasswordHash(passwordEncoder.encode("password"));


        Exception e = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertTrue(e.getMessage().contains("users_username_key"));

        user.setUsername("testUser1");
        user.setEmail("test@email.com");

        e = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
        assertTrue(e.getMessage().contains("users_email_key"));

        assertEquals(1, userRepository.count());
    }

    @Order(4)
    @Test
    public void read_TestMethod() {
        assertEquals(1, userRepository.count());

        List<User> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertEquals("testUsername", users.get(0).getUsername());
        assertEquals("test@email.com", users.get(0).getEmail());

        User user = new User();
        user.setUsername("newUser");
        user.setEmail("newUser@email.com");
        user.setPasswordHash(passwordEncoder.encode("password"));
        userRepository.save(user);

        users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Order(5)
    @Test
    public void findUserByUsername_TestMethod() {
        assertEquals(2, userRepository.count());

        Optional<User> user = userRepository.findByUsername("testUsername");

        assertTrue(user.isPresent());
        assertEquals("testUsername", user.get().getUsername());
        assertEquals("test@email.com", user.get().getEmail());
    }

    @Order(6)
    @Test
    public void delete_TestMethod() {
        assertEquals(2, userRepository.count());

        Optional<User> user = userRepository.findByUsername("testUsername");

        assertTrue(user.isPresent());

        userRepository.delete(user.get());

        assertTrue(userRepository.findByUsername("testUsername").isEmpty());

        assertEquals(1, userRepository.count());

        userRepository.deleteAll();
        assertEquals(0, userRepository.count());
    }

}
