package net.ancronik.cookbook.backend.authentication.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import java.time.Duration;

/**
 * Configuration for access/refresh tokens.
 *
 * @author Nikola Presecki
 */
@Configuration
public class TokenSettingsConfiguration {

    @Primary
    @Bean
    public TokenSettings tokenSettings(@Value("${oauth2.access.token.ttl.hours}") long accessTokenTtlHours,
                                       @Value("${oauth2.refresh.token.ttl.hours}") long refreshTokenTtlHours) {

        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(accessTokenTtlHours))
                .refreshTokenTimeToLive(Duration.ofHours(refreshTokenTtlHours))
                .build();
    }

}
