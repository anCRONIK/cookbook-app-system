package net.ancronik.cookbook.backend.integrationtest;

import org.testcontainers.containers.CassandraContainer;

public class CustomCassandraContainer extends CassandraContainer<CustomCassandraContainer> {

    private static final String IMAGE_VERSION = "cassandra:4.0.3";

    public static final int CASSANDRA_PORT = 9042;


    private static CustomCassandraContainer container;


    private CustomCassandraContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomCassandraContainer getInstance() {
        if (container == null) {
            container = new CustomCassandraContainer().withExposedPorts(CASSANDRA_PORT).withInitScript("setup_db.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("cassandra_host", container.getContainerIpAddress());
        System.setProperty("cassandra_port", String.valueOf(container.getMappedPort(CASSANDRA_PORT)));
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }


}
