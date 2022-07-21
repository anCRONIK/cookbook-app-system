package net.ancronik.cookbook.backend.integrationtest.data.repository;

import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.integrationtest.BaseIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecipeCategoryRepositoryIT extends BaseIntegrationTest {

    @Autowired
    RecipeCategoryRepository recipeCategoryRepository;

    @Test
    void readEntries_AllEntriesFromDatabaseShouldBeLoaded() {
        List<RecipeCategory> recipeCategoryList = recipeCategoryRepository.findAll();

        assertNotNull(recipeCategoryList);
        assertTrue(recipeCategoryList.size() > 2);
    }

    @Test
    void findById_MultipleTests() {
        assertTrue(recipeCategoryRepository.findById("test").isEmpty());
        assertTrue(recipeCategoryRepository.findById("dessert").isPresent());
    }

}
