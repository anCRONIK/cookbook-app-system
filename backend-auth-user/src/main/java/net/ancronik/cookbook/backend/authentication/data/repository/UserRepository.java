package net.ancronik.cookbook.backend.authentication.data.repository;

import net.ancronik.cookbook.backend.authentication.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link User}.
 *
 * @author Nikola Presecki
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

}