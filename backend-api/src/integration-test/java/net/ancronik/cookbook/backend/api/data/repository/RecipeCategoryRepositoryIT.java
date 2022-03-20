package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.DatabaseIntegrationTest;
import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class RecipeCategoryRepositoryIT extends DatabaseIntegrationTest {

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
