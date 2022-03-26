package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model representing basic user which can be extended by real entities.
 *
 * @author Nikola Presecki
 */
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BasicUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotBlank
    @CodePointLength(min = 2, max = 12)
    @Column(name = "username", unique = true, updatable = false)
    private String username;

    @NotBlank
    @CodePointLength(min = 4, max = 30)
    @Email
    @Column(name = "email", unique = true, updatable = false)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "is_locked")
    private boolean accountLocked;

    @Column(name = "is_disabled")
    private boolean accountDisabled;

    @Column(name = "require_password_reset")
    private boolean passwordResetRequired;

}