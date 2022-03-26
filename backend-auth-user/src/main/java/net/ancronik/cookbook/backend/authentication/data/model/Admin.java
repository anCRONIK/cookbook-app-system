package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Model representing users.
 *
 * @author Nikola Presecki
 */
@Entity
@Table(name = "admins")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends BasicUser {

    @Transient
    @OneToOne(mappedBy = "admin_id")
    private LoginAttempts loginAttempts;

}