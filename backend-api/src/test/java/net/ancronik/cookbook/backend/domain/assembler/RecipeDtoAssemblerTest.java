package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.data.model.*;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class RecipeDtoAssemblerTest {

    RecipeDtoAssembler assembler = new RecipeDtoAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }

    @Test
    public void toModel_ModelWithoutIngredients_ValidDtoReturned() {
        Recipe recipe = new Recipe(1L, "title", "short desc value", null, null, null,
                10, "slice everything into the bowl",
                30, "throw sliced veggies in boiling water and cook for 30 minutes",
                LocalDateTime.of(2022, 3, 6, 11, 23), null,
                1, new RecipeCategory("entree"), "pero");

        RecipeDto dto = assembler.toModel(recipe);
        assertEquals("RecipeDto(id=1, title=title, shortDescription=short desc value, coverImageUrl=null, thumbnailUrl=null, ingredientList=null, preparationTime=10, preparationInstructions=slice everything into the bowl, cookingTime=30, cookingInstructions=throw sliced veggies in boiling water and cook for 30 minutes, dateCreated=2022-03-06T11:23, lastUpdated=null, difficulty=1, category=entree, authorId=pero)", dto.toString());
        assertEquals(3, dto.getLinks().toList().size());
    }

    @Test
    public void toModel_ModelWithEmptyIngredientsList_ValidDtoReturned() {
        Recipe recipe = new Recipe(1L, "title", "short desc value", null, null, new ArrayList<>(),
                10, "slice everything into the bowl",
                30, "throw sliced veggies in boiling water and cook for 30 minutes",
                LocalDateTime.of(2022, 3, 6, 11, 23), null,
                1, new RecipeCategory("entree"), "pero");

        RecipeDto dto = assembler.toModel(recipe);
        assertEquals("RecipeDto(id=1, title=title, shortDescription=short desc value, coverImageUrl=null, thumbnailUrl=null, ingredientList=[], preparationTime=10, preparationInstructions=slice everything into the bowl, cookingTime=30, cookingInstructions=throw sliced veggies in boiling water and cook for 30 minutes, dateCreated=2022-03-06T11:23, lastUpdated=null, difficulty=1, category=entree, authorId=pero)", dto.toString());
        assertEquals(3, dto.getLinks().toList().size());
    }

    @Test
    public void toModel_ModelWithIngredients_ValidDtoReturned() {
        Recipe recipe = new Recipe(1L, "title", "short desc value", null, null,
                List.of(new Ingredient("ing1", "2/3", "cup"),
                        new Ingredient("ing2", "1.5", "kg")),
                10, "slice everything into the bowl",
                30, "throw sliced veggies in boiling water and cook for 30 minutes",
                LocalDateTime.of(2022, 3, 6, 11, 23), null,
                1, new RecipeCategory("entree"), "pero");

        RecipeDto dto = assembler.toModel(recipe);
        assertEquals("RecipeDto(id=1, title=title, shortDescription=short desc value, coverImageUrl=null, thumbnailUrl=null, ingredientList=[IngredientDto(name=ing1, quantity=2/3, measurementUnit=cup), IngredientDto(name=ing2, quantity=1.5, measurementUnit=kg)], preparationTime=10, preparationInstructions=slice everything into the bowl, cookingTime=30, cookingInstructions=throw sliced veggies in boiling water and cook for 30 minutes, dateCreated=2022-03-06T11:23, lastUpdated=null, difficulty=1, category=entree, authorId=pero)", dto.toString());
        assertEquals(3, dto.getLinks().toList().size());
    }
}
