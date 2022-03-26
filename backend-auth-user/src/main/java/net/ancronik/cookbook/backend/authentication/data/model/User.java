package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Model representing user.
 *
 * @author Nikola Presecki
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BasicUser {

    @Transient
    @OneToOne(mappedBy = "user_id")
    private LoginAttempts loginAttempts;

    @Column(name = "is_editor")
    protected boolean isEditor;

}