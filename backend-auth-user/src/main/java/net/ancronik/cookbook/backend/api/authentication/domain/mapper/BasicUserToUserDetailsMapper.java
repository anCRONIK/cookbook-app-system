package net.ancronik.cookbook.backend.api.authentication.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.authentication.application.security.SecurityAuthorities;
import net.ancronik.cookbook.backend.api.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.api.authentication.data.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Mapper for creating {@link UserDetails} from {@link User}.
 *
 * @author Nikola Presecki
 */
@Component
public class BasicUserToUserDetailsMapper implements Mapper<BasicUser, UserDetails> {

    @Override
    public UserDetails map(@NonNull BasicUser user) {

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                null,
                !user.isAccountDisabled(),
                !user.isAccountLocked(),
                !user.isAccountLocked(),
                !user.isAccountLocked(),
                SecurityAuthorities.getAuthoritiesForUser(user)
        );
    }
}
