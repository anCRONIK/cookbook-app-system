package net.ancronik.cookbook.backend.api.authentication.application.security;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.authentication.data.model.Admin;
import net.ancronik.cookbook.backend.api.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.api.authentication.data.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
     * Admin authority
     */
    public static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority(ADMIN_ROLE);

    /**
     * Editor authority
     */
    public static final GrantedAuthority EDITOR_AUTHORITY = new SimpleGrantedAuthority(EDITOR_ROLE);


    /**
     * User authority
     */
    public static final GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority(USER_ROLE);


    /**
     * Method for finding user authorities.
     * <p>
     * Throws {@code IllegalArgumentException} in case of invalid {@code user} instance.
     *
     * @param user instance of {@link User} or {@link Admin}
     * @return list of authorities
     */
    public static List<GrantedAuthority> getAuthoritiesForUser(@NonNull BasicUser user) {
        if (user instanceof Admin) {
            return List.of(ADMIN_AUTHORITY);
        } else if (user instanceof User) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(USER_AUTHORITY);

            if (((User) user).isEditor()) {
                authorities.add(EDITOR_AUTHORITY);
            }

            return authorities;
        } else {
            throw new IllegalArgumentException("Given user is not of type admin or user");
        }
    }
}
