package net.ancronik.cookbook.backend.authentication.domain.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.authentication.data.model.BasicUser;
import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.AdminRepository;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import net.ancronik.cookbook.backend.authentication.domain.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService}
 *
 * @author Nikola Presecki
 */
@Service
@Slf4j
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private final Mapper<BasicUser, UserDetails> basicUserToUserDetailsMapper;

    /**
     * Constructor
     *
     * @param userRepository               user repository
     * @param adminRepository              admin repository
     * @param basicUserToUserDetailsMapper mapper
     */
    @Autowired
    public SecurityUserDetailsService(UserRepository userRepository, AdminRepository adminRepository, Mapper<BasicUser, UserDetails> basicUserToUserDetailsMapper) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.basicUserToUserDetailsMapper = basicUserToUserDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        if (username.isBlank()) {
            throw new UsernameNotFoundException("username is blank");
        }
        LOG.debug("Searching for user {}", username);
        BasicUser basicUser;
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            LOG.info("User not found in table, checking admins");
            basicUser = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        } else {
            basicUser = user.get();
        }

        LOG.info("User found {}", user);
        return basicUserToUserDetailsMapper.map(basicUser);
    }

}