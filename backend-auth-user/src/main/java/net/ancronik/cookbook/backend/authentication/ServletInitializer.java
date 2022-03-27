package net.ancronik.cookbook.backend.authentication;

import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * Servlet initializer for external application server support.
 *
 * @author Nikola Presecki
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UserAuthSpringBootApp.class);
    }
}
