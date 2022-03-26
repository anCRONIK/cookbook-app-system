package net.ancronik.cookbook.backend.authentication.integrationtest;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresqlContainer extends PostgreSQLContainer<CustomPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";

    private static CustomPostgresqlContainer container;

    private CustomPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgresqlContainer getInstance() {
        if (container == null) {
            container = new CustomPostgresqlContainer().withInitScript("setup_db.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("postgres_url", container.getJdbcUrl());
        System.setProperty("postgres_username", container.getUsername());
        System.setProperty("postgres_password", container.getPassword());


    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}