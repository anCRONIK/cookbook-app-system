package net.ancronik.cookbook.backend.api.authentication.data.model;

import lombok.*;
import org.hibernate.validator.constraints.CodePointLength;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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