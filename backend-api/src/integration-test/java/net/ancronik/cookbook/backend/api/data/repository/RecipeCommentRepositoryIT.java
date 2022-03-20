package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.DatabaseIntegrationTest;
import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentPK;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeCommentRepositoryIT extends DatabaseIntegrationTest {

    @Autowired
    RecipeCommentRepository recipeCommentRepository;


    @Order(1)
    @Test
    public void createMultipleComments() {
        recipeCommentRepository.saveAll(RecipeCommentMockData.generateRandomMockData(7));
    }

    @Order(2)
    @Test
    public void readAllSavedCommentsInDatabase_TestPageable() {
        int counter = 0;
        Slice<RecipeComment> comments = recipeCommentRepository.findAll(Pageable.ofSize(2));
        counter += comments.getNumberOfElements();

        do {
            comments = recipeCommentRepository.findAll(comments.nextPageable());
            counter += comments.getNumberOfElements();
        } while (comments.hasNext());

        assertEquals(7, counter);
    }

    @Order(3)
    @Test
    public void InvalidPrimaryKey_DatabaseThrowsException() {
        RecipeCommentPK pk = new RecipeCommentPK();
        RecipeComment comment = new RecipeComment(pk, "wuhuuuuu");

        assertThrows(DataAccessException.class, () -> recipeCommentRepository.save(comment));

        pk.setRecipeId(1L);
        assertThrows(DataAccessException.class, () -> recipeCommentRepository.save(comment));

        pk.setUsername("test");
        assertThrows(DataAccessException.class, () -> recipeCommentRepository.save(comment));

        pk.setDateCreated(LocalDateTime.now());
        assertDoesNotThrow(() -> recipeCommentRepository.save(comment));
    }

    @Order(4)
    @Test
    public void deleteSpecificComment() {
        recipeCommentRepository.deleteById(recipeCommentRepository.findAll().get(0).getRecipeCommentPK());
        assertEquals(7, recipeCommentRepository.count());
    }

    @Order(5)
    @Test
    public void findAllByRecipeCommentPKRecipeId() {
        RecipeCommentPK pk = new RecipeCommentPK(9999999L, "testAuthor", LocalDate.now().atStartOfDay());
        RecipeComment comment = new RecipeComment(pk, "wuhuuuuu");

        recipeCommentRepository.save(comment);
        Slice<RecipeComment> comments = recipeCommentRepository.findAllByRecipeCommentPKRecipeId(pk.getRecipeId(), Pageable.ofSize(10));

        assertEquals(1, comments.getNumberOfElements());
        assertEquals(comment, comments.getContent().get(0));
    }

    @Order(6)
    @Test
    public void testThatDeleteAllCommentIsNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> recipeCommentRepository.deleteAll());
    }

}
