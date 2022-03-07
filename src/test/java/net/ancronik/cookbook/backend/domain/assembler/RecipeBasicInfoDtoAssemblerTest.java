package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.data.model.*;
import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class RecipeBasicInfoDtoAssemblerTest {

    RecipeBasicInfoDtoAssembler assembler = new RecipeBasicInfoDtoAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }


    @Test
    public void toModel_ModelWithIngredients_ValidDtoReturned() {
        Recipe recipe = new Recipe(1L, "title", "short desc value", null,
                List.of(new Ingredient("ing1", new IngredientQuantity("2/3", MeasurementUnit.CUP)),
                        new Ingredient("ing2", new IngredientQuantity("1.5", MeasurementUnit.KG))),
                10, "slice everything into the bowl",
                30, "throw sliced veggies in boiling water and cook for 30 minutes",
                LocalDateTime.of(2022, 3, 6, 11, 23), null,
                RecipeDifficulty.LOW, RecipeCategory.ENTREE, "pero");

        RecipeBasicInfoDto dto = assembler.toModel(recipe);
        assertEquals("RecipeBasicInfoDto(id=1, title=title, shortDescription=short desc value, coverImageUrl=null, dateCreated=2022-03-06T11:23, preparationTime=10, cookingTime=30, difficulty=1, category=ENTREE, authorId=pero)", dto.toString());
        assertEquals(2, dto.getLinks().toList().size());
    }
}
