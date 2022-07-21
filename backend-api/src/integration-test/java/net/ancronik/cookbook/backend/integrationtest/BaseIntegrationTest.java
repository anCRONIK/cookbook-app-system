package net.ancronik.cookbook.backend.integrationtest;

import net.ancronik.cookbook.backend.api.CookbookBackendApiSpringBootApp;
import net.ancronik.cookbook.backend.api.TestTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest(classes = CookbookBackendApiSpringBootApp.class)
@ExtendWith({SpringExtension.class, CassandraTestContainersExtension.class})
@Tag(TestTypes.INTEGRATION)
public class BaseIntegrationTest {

    @BeforeAll
    static void beforeAll(List<CrudRepository<?, ?>> repositories) throws Exception {
        repositories.forEach(CrudRepository::deleteAll);
    }

}
