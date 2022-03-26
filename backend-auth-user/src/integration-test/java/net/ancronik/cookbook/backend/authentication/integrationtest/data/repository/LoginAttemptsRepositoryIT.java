package net.ancronik.cookbook.backend.authentication.integrationtest.data.repository;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.UserAuthSpringBootApp;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.model.LoginAttempts;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.AdminRepository;
import net.ancronik.cookbook.backend.authentication.data.repository.LoginAttemptsRepository;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import net.ancronik.cookbook.backend.authentication.integrationtest.PostgresTestContainersExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({PostgresTestContainersExtension.class, SpringExtension.class})
@SpringBootTest(classes = UserAuthSpringBootApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TestTypes.INTEGRATION)
public class LoginAttemptsRepositoryIT {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    LoginAttemptsRepository loginAttemptsRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Order(1)
    @Test
    public void testInitUserAndAdminRepo() {
        assertEquals(0, userRepository.count());
        assertEquals(0, adminRepository.count());

        User user = new User();
        user.setUsername("fuyhg");
        user.setEmail("jsa@email.com");
        user.setPasswordHash(passwordEncoder.encode("pass"));

        Admin admin = new Admin();
        admin.setUsername("fds");
        admin.setEmail("fds@email.com");
        admin.setPasswordHash(passwordEncoder.encode("pass"));


        adminRepository.save(admin);
        userRepository.save(user);

        assertEquals(1, userRepository.count());
        assertEquals(1, adminRepository.count());
    }

    @Order(2)
    @Test
    public void save_TestAdminForeignKey() {
        Admin admin = adminRepository.findByUsername("fds").orElseThrow();
        LoginAttempts loginAttempts = new LoginAttempts();
        loginAttempts.setAdminId(admin.getId());

        loginAttemptsRepository.save(loginAttempts);

        LoginAttempts loginAttempts2 = new LoginAttempts();
        loginAttempts2.setAdminId(admin.getId());

        Exception e = assertThrows(DataIntegrityViolationException.class, () -> loginAttemptsRepository.save(loginAttempts2));
        assertTrue(e.getMessage().contains("login_attempts_admin_id_key"));

        LoginAttempts loginAttempts3 = new LoginAttempts();
        loginAttempts3.setAdminId(333L);

        e = assertThrows(DataIntegrityViolationException.class, () -> loginAttemptsRepository.save(loginAttempts3));
        assertTrue(e.getMessage().contains("fk_admin"));
    }

    @Order(2)
    @Test
    public void save_TestUserForeignKey() {
        User user = userRepository.findByUsername("fuyhg").orElseThrow();
        LoginAttempts loginAttempts = new LoginAttempts();
        loginAttempts.setUserId(user.getId());

        loginAttemptsRepository.save(loginAttempts);

        LoginAttempts loginAttempts2 = new LoginAttempts();
        loginAttempts2.setUserId(user.getId());

        Exception e = assertThrows(DataIntegrityViolationException.class, () -> loginAttemptsRepository.save(loginAttempts2));
        assertTrue(e.getMessage().contains("login_attempts_user_id_key"));

        LoginAttempts loginAttempts3 = new LoginAttempts();
        loginAttempts3.setUserId(333L);

        e = assertThrows(DataIntegrityViolationException.class, () -> loginAttemptsRepository.save(loginAttempts3));
        assertTrue(e.getMessage().contains("fk_user"));
    }

    @Order(3)
    @Test
    public void findByUserId_MultipleScenarios() {
        User user = userRepository.findByUsername("fuyhg").orElseThrow();

        assertTrue(loginAttemptsRepository.findByUserId(user.getId()).isPresent());
        assertFalse(loginAttemptsRepository.findByUserId(233213213L).isPresent());
    }

    @Order(3)
    @Test
    public void findAdminId_MultipleScenarios() {
        Admin admin = adminRepository.findByUsername("fds").orElseThrow();

        assertTrue(loginAttemptsRepository.findByAdminId(admin.getId()).isPresent());
        assertFalse(loginAttemptsRepository.findByAdminId(2332L).isPresent());
    }

    @Order(4)
    @Test
    public void adminDeleted_EntryShouldBeAlsoDeleted() {
        Admin admin = adminRepository.findByUsername("fds").orElseThrow();

        assertTrue(loginAttemptsRepository.findByAdminId(admin.getId()).isPresent());
        adminRepository.delete(admin);
        assertFalse(loginAttemptsRepository.findByAdminId(admin.getId()).isPresent());
    }

    @Order(4)
    @Test
    public void userDeleted_EntryShouldBeAlsoDeleted() {
        User user = userRepository.findByUsername("fuyhg").orElseThrow();

        assertTrue(loginAttemptsRepository.findByUserId(user.getId()).isPresent());
        userRepository.delete(user);
        assertFalse(loginAttemptsRepository.findByUserId(user.getId()).isPresent());
    }

}
