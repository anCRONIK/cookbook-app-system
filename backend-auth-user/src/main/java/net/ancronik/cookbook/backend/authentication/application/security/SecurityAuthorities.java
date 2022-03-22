package net.ancronik.cookbook.backend.authentication.application.security;

import lombok.NonNull;
import net.ancronik.cookbook.backend.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Authorities used in the application.
 *
 * @author Nikola Presecki
 */
public abstract class SecurityAuthorities {

    /**
     * Name of the admin role.
     */
    public static final String ADMIN_ROLE = "ADMIN";

    /**
     * Name of the editor role.
     */
    public static final String EDITOR_ROLE = "EDITOR";

    /**
     * Name of the user role.
     */
    public static final String USER_ROLE = "USER";


    /**
     * Method for finding user authorities.
     * <p>
     * Throws {@code IllegalArgumentException} in case of invalid {@code user} instance.
     *
     * @param user instance of {@link User} or {@link Admin}
     * @return list of authorities
     */
    public static List<String> getRolesForUser(@NonNull BasicUser user) {
        if (user instanceof Admin) {
            return List.of(ADMIN_ROLE);
        } else if (user instanceof User) {
            List<String> authorities = new ArrayList<>();
            authorities.add(USER_ROLE);

            if (((User) user).isEditor()) {
                authorities.add(EDITOR_ROLE);
            }

            return authorities;
        } else {
            throw new IllegalArgumentException("Given user is not of type admin or user");
        }
    }
}
