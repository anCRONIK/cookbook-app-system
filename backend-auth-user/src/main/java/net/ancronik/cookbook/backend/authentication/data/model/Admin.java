package net.ancronik.cookbook.backend.authentication.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model representing users.
 *
 * @author Nikola Presecki
 */
@Entity
@Table(name = "admins")
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends BasicUser {

}