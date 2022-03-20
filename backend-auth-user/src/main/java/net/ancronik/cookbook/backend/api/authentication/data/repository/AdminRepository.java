package net.ancronik.cookbook.backend.api.authentication.data.repository;

import net.ancronik.cookbook.backend.api.authentication.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Admin}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findAdminByUsername(String username);

}