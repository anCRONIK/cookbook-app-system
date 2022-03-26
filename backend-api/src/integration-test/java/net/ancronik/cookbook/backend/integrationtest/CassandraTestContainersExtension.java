package net.ancronik.cookbook.backend.integrationtest;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CassandraTestContainersExtension implements BeforeAllCallback {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (CassandraTestContainersExtension.class) {
            if (!started) {
                started = true;

                CustomCassandraContainer.getInstance().start();
            }
        }
    }
}
