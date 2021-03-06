package net.ancronik.cookbook.backend.api.domain.assembler;

import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import net.ancronik.cookbook.backend.api.data.model.RecipeCommentPK;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCommentModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(TestTypes.UNIT)
class RecipeCommentModelAssemblerTest {

    RecipeCommentModelAssembler assembler = new RecipeCommentModelAssembler(new ModelMapper());

    @Test
    void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }

    @Test
    void toModel_ModelWithoutIngredients_ValidDtoReturned() {
        RecipeComment comment = new RecipeComment(new RecipeCommentPK(1L, "pero", LocalDateTime.of(2022, 3, 6, 16, 46)), "WOW! Awesome recipe.");

        RecipeCommentModel dto = assembler.toModel(comment);
        assertEquals("RecipeCommentModel(username=pero, text=WOW! Awesome recipe., dateCreated=2022-03-06T16:46)", dto.toString());
        assertEquals(1, dto.getLinks().toList().size());
    }

}
