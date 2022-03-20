package net.ancronik.cookbook.backend.api.authentication.data.model;

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
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends BasicUser {

}