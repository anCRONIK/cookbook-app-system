package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.TestConfigurationForUnitTesting;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.web.dto.recipe.AddRecipeCommentRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationForUnitTesting.class)
@Tag(TestTypes.UNIT)
public class RecipeCommentServiceImplValidationTest {

    @Autowired
    private RecipeCommentService recipeCommentService;

    @Test
    public void getCommentsForRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(-1L, Pageable.ofSize(10)));
        assertEquals("getCommentsForRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }

    @Test
    public void getCommentsForRecipe_InvalidPageableGiven_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(2L, Pageable.unpaged()));
        assertEquals("getCommentsForRecipe.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.getCommentsForRecipe(2L, Pageable.ofSize(110)));
        assertEquals("getCommentsForRecipe.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());
    }

    @Test
    public void addCommentToRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(-231L, new AddRecipeCommentRequest("valid text")));
        assertEquals("addCommentToRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }

    @Test
    public void addCommentToRecipe_InvalidComment_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest()));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("        ")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest("   \n \t  ")));
        assertEquals("addCommentToRecipe.comment.text: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeCommentService.addCommentToRecipe(10L, new AddRecipeCommentRequest(StringTestUtils.getRandomStringInLowerCase(21312321))));
        assertEquals("addCommentToRecipe.comment.text: length must be between 0 and 10000", t.getMessage());
    }

}
