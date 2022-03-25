package net.ancronik.cookbook.backend.authentication.integrationtest;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PostgresTestContainersExtension implements BeforeAllCallback {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (PostgresTestContainersExtension.class) {
            if (!started) {
                started = true;

                CustomPostgresqlContainer.getInstance().start();

                context.getRoot()
                        .getStore(ExtensionContext.Namespace.GLOBAL)
                        .put("TestContainersExtension-started", this);
            }
        }
    }
}
