package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.Data;
import net.ancronik.cookbook.backend.authentication.validation.annotation.CombinedNotNull;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Model that tracks user invalid login attempts.
 *
 * @author Nikola Presecki
 */
@Data
@Entity
@Table(name = "login_attempts")
@CombinedNotNull(fields = {"userId", "adminId"})
public class LoginAttempts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true, updatable = false)
    private Long userId;

    @Column(name = "admin_id", unique = true, updatable = false)
    private Long adminId;

    @Column(name = "attempt_counter")
    @Range(max = 10)
    private int attemptCounter;
}