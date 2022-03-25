package net.ancronik.cookbook.backend.authentication.integrationtest.data.repository;

import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.repository.AdminRepository;
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
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TestTypes.INTEGRATION)
public class AdminRepositoryIT {

    @Autowired
    AdminRepository adminRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Order(1)
    @Test
    public void save_TestConstraintsThatAreNotCoveredByValidation() {
        assertEquals(0, adminRepository.count());

        Admin admin = new Admin();
        admin.setUsername("testUsername");
        admin.setEmail("test@email.com");


        Exception e = assertThrows(DataIntegrityViolationException.class, () -> adminRepository.save(admin));
        assertTrue(e.getMessage().contains("constraint [password_hash]"));

        assertEquals(0, adminRepository.count());
    }

    @Order(2)
    @Test
    public void save_CreateSingleAdmin() {
        assertEquals(0, adminRepository.count());

        Admin admin = new Admin();
        admin.setUsername("testUsername");
        admin.setEmail("test@email.com");
        admin.setPasswordHash(passwordEncoder.encode("password"));


        Admin savedAdmin = adminRepository.save(admin);
        assertNotNull(savedAdmin);

        assertEquals(1, adminRepository.count());
    }

    @Order(3)
    @Test
    public void save_TestUniqueConstraints() {
        assertEquals(1, adminRepository.count());

        Admin admin = new Admin();
        admin.setUsername("testUsername");
        admin.setEmail("test1@email.com");
        admin.setPasswordHash(passwordEncoder.encode("password"));


        Exception e = assertThrows(DataIntegrityViolationException.class, () -> adminRepository.save(admin));
        assertTrue(e.getMessage().contains("admins_username_key"));

        admin.setUsername("testUser1");
        admin.setEmail("test@email.com");

        e = assertThrows(DataIntegrityViolationException.class, () -> adminRepository.save(admin));
        assertTrue(e.getMessage().contains("admins_email_key"));

        assertEquals(1, adminRepository.count());
    }

}
