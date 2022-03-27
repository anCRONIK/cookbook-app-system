package net.ancronik.cookbook.backend.authentication;

import net.ancronik.cookbook.backend.authentication.data.model.User;
import net.ancronik.cookbook.backend.authentication.data.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * Main application.
 *
 * @author Nikola Presecki
 */
@SpringBootApplication
public class UserAuthSpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(UserAuthSpringBootApp.class, args);
    }

}
