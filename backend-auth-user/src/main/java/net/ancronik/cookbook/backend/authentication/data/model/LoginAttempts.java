package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Model that tracks user invalid login attempts.
 *
 * @author Nikola Presecki
 */
@Data
@Entity
@Table(name = "login_attempts")
public class LoginAttempts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id",unique = true, updatable = false)
    @NotNull
    private Long userId;

    @Column(name = "admin_id" ,unique = true, updatable = false)
    @NotNull
    private Long adminId;

    @Column(name = "attempt_counter")
    @Range(max = 10)
    private int attemptCounter;
}