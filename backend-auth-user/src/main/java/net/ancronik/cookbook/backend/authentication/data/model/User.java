package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.*;

import javax.persistence.*;

/**
 * Model representing user.
 *
 * @author Nikola Presecki
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BasicUser {

    @Column(name = "is_editor")
    protected boolean isEditor;

}