package net.ancronik.cookbook.backend.authentication.application.security;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.authentication.application.exception.InternalException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Utility class for generating keys.
 *
 * @author Nikola Presecki
 */
@Slf4j
public final class KeyGeneratorUtils {

    private static final int DEFAULT_RSA_KEY_SIZE = 2048;

    private KeyGeneratorUtils() {
    }

    /**
     * Method for creating new instance of RSA key with key size of 2048.
     *
     * @return generated key pair
     */
    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(DEFAULT_RSA_KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            LOG.error("Error while generating RSA key", ex);
            throw new InternalException(ex);
        }
        return keyPair;
    }
}