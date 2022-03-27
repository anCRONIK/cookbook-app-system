package net.ancronik.cookbook.backend.authentication.application.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;


final class KeyGeneratorUtils {

    private KeyGeneratorUtils() {
    }

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}