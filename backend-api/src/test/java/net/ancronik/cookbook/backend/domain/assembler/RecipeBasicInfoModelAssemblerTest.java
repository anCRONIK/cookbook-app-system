package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.data.model.Ingredient;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class RecipeBasicInfoModelAssemblerTest {

    RecipeBasicInfoModelAssembler assembler = new RecipeBasicInfoModelAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
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

        RecipeBasicInfoModel dto = assembler.toModel(recipe);
        assertEquals("RecipeBasicInfoModel(id=1, title=title, shortDescription=short desc value, thumbnailUrl=null, dateCreated=2022-03-06T11:23, preparationTimeInMinutes=10, cookingTimeInMinutes=30, difficulty=1, category=entree, authorId=pero)", dto.toString());
        assertEquals(3, dto.getLinks().toList().size());
    }
}
