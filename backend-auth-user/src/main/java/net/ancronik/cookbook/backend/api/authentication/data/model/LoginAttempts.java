package net.ancronik.cookbook.backend.api.authentication.data.model;

import lombok.Data;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    @Column(unique = true, updatable = false, nullable = false)
    @NotNull
    private Long userId;

    @Column(name = "attempt_counter")
    @Range(max = 10)
    private int attemptCounter;
}