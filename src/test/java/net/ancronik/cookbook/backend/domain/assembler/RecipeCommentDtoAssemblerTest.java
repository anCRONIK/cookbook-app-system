package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.data.model.*;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCommentDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class RecipeCommentDtoAssemblerTest {

    RecipeCommentDtoAssembler assembler = new RecipeCommentDtoAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }

    @Test
    public void toModel_ModelWithoutIngredients_ValidDtoReturned() {
        RecipeComment comment = new RecipeComment(1L, "pero", "WOW! Awesome recipe.", LocalDateTime.of(2022, 3,6,16,46));

        RecipeCommentDto dto = assembler.toModel(comment);
        assertEquals("RecipeCommentDto(username=pero, text=WOW! Awesome recipe., dateCreated=2022-03-06T16:46)", dto.toString());
        assertEquals(1, dto.getLinks().toList().size());
    }

}
