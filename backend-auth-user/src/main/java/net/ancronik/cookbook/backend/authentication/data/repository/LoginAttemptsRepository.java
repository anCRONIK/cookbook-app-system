package net.ancronik.cookbook.backend.authentication.data.repository;

import net.ancronik.cookbook.backend.authentication.data.model.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Nikola Presecki
 */
@Repository
public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Integer> {

    Optional<LoginAttempts> findByUserId(Long userId);

    Optional<LoginAttempts> findByAdminId(Long userId);

}