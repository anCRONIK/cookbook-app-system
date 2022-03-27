package net.ancronik.cookbook.backend.authentication.application.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application configuration.
 *
 * @author Nikola Presecki
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@EnableCaching
public class AppConfig {


}
