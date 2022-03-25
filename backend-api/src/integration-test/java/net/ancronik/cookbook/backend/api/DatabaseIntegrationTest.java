package net.ancronik.cookbook.backend.api;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
@Testcontainers
@Tag(TestTypes.INTEGRATION)
@Slf4j
public abstract class DatabaseIntegrationTest {

    public static final String KEYSPACE_NAME = "cookbook";

    public static final int CASSANDRA_PORT = 9042;

    private static final String DB_INIT_SQL_FILENAME = "/setup_db.sql";

    @Container
    public static final CassandraContainer cassandra = (CassandraContainer) new CassandraContainer("cassandra:3.11.2")
            .withExposedPorts(CASSANDRA_PORT);


    @DynamicPropertySource
    static void setupCassandraConnectionProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.keyspace-name", () -> KEYSPACE_NAME);
        registry.add("spring.data.cassandra.contact-points", cassandra::getContainerIpAddress);
        registry.add("spring.data.cassandra.port", () -> String.valueOf(cassandra.getMappedPort(9042)));
    }

    @BeforeAll
    static void init() {
        createKeyspace(cassandra.getCluster());
        setupDatabase(cassandra.getCluster());
    }

    static void createKeyspace(Cluster cluster) {
        try (Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME + " WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};");
        }
    }


    @SneakyThrows
    private static void setupDatabase(Cluster cluster) {
        String DELIMITER = ";";
        String COMMENT = "--";
        try (Session session = cluster.connect()) {
            File script = new ClassPathResource(DB_INIT_SQL_FILENAME).getFile();
            List<String> lines = Files.readAllLines(script.toPath());
            StringBuilder command = new StringBuilder();
            for (String line : lines) {
                if (StringUtils.hasText(line) && !line.startsWith(COMMENT)) {
                    command.append(line);

                    if (line.endsWith(DELIMITER)) {
                        LOG.debug("Executing sql: {}", command);
                        session.execute(command.toString());
                        command.setLength(0);
                    }
                }
            }
        }
    }


    @Order(0)
    @Test
    void givenCassandraContainer_SpringContextIsBootstrapped_ContainerIsRunningWithNoExceptions() {
        assertThat(cassandra.isRunning()).isTrue();
    }
}
