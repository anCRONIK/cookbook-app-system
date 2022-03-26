package net.ancronik.cookbook.backend.integrationtest.data.repository;

import net.ancronik.cookbook.backend.api.CookbookBackendApiSpringBootApp;
import net.ancronik.cookbook.backend.integrationtest.CassandraTestContainersExtension;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCategoryRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = CookbookBackendApiSpringBootApp.class)
@ExtendWith({SpringExtension.class, CassandraTestContainersExtension.class})
@Tag(TestTypes.INTEGRATION)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeCategoryRepositoryIT {

    @Autowired
    RecipeCategoryRepository recipeCategoryRepository;

    @Test
    public void readEntries_AllEntriesFromDatabaseShouldBeLoaded() {
        List<RecipeCategory> recipeCategoryList = recipeCategoryRepository.findAll();

        assertNotNull(recipeCategoryList);
        assertTrue(recipeCategoryList.size() > 2);
    }

    @Test
    public void findById_MultipleTests() {
        assertTrue(recipeCategoryRepository.findById("test").isEmpty());
        assertTrue(recipeCategoryRepository.findById("dessert").isPresent());
    }

    @Test
    public void testThatWriteOperationsAreNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.save(new RecipeCategory("test")));
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.delete(new RecipeCategory("test")));
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.saveAll(List.of(new RecipeCategory("test"))));
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.deleteAll(List.of(new RecipeCategory("test"))));
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.deleteAll());
        assertThrows(UnsupportedOperationException.class, () -> recipeCategoryRepository.deleteAllById(List.of("id")));
    }

}
