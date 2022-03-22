package net.ancronik.cookbook.backend.authentication.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.authentication.application.security.SecurityAuthorities;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;
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

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password("")
                .disabled(user.isAccountDisabled())
                .accountLocked(user.isAccountLocked())
                .roles(SecurityAuthorities.getRolesForUser(user).toArray(String[]::new)).build();
    }
}
