package net.ancronik.cookbook.backend.api.domain.mapper;

import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@Tag(TestTypes.UNIT)
class RecipeMapperTest {

    private RecipeMapper mapper = new RecipeMapper(new ModelMapper());

    @Test
    void map_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(null));
    }

    @Test
    void map_DtoGiven_ReturnPopulatedModel() {
        RecipeCreateRequest request = DtoMockData.generateRandomMockDataForRecipeCreateRequest();

        Recipe recipe = mapper.map(request);

        assertNull(recipe.getId());
        assertEquals(request.getTitle(), recipe.getTitle());
        assertEquals(request.getShortDescription(), recipe.getShortDescription());
        assertEquals(request.getCoverImageUrl(), recipe.getCoverImageUrl());
        assertEquals(request.getThumbnailUrl(), recipe.getThumbnailUrl());
        assertEquals(request.getIngredientList().size(), recipe.getIngredientList().size());
        for (int i = 0; i < request.getIngredientList().size(); ++i) {
            assertEquals(request.getIngredientList().get(i).getName(), recipe.getIngredientList().get(i).getName());
            assertEquals(request.getIngredientList().get(i).getQuantity(), recipe.getIngredientList().get(i).getQuantity());
            assertEquals(request.getIngredientList().get(i).getMeasurementUnit(), recipe.getIngredientList().get(i).getMeasurementUnit());
        }
        assertEquals(request.getPreparationInstructions(), recipe.getPreparationInstructions());
        assertEquals(request.getPreparationTimeInMinutes(), recipe.getPreparationTimeInMinutes());
        assertEquals(request.getCookingTimeInMinutes(), recipe.getCookingTimeInMinutes());
        assertEquals(request.getCookingInstructions(), recipe.getCookingInstructions());
        assertNull(recipe.getDateCreated());
        assertNull(recipe.getDateLastUpdated());
        assertEquals(request.getDifficulty(), recipe.getDifficulty());
        assertEquals(request.getCategory(), recipe.getCategory().getCategory());
        assertNull(recipe.getAuthorId());

    }

    @Test
    void update_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mapper.update(null, new Recipe()));
        assertThrows(IllegalArgumentException.class, () -> mapper.update(new RecipeUpdateRequest(), null));
    }

    @Test
    void update_DtoGiven_ReturnPopulatedModel() {
        RecipeUpdateRequest request = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();

        Recipe recipe = new Recipe();

        mapper.update(request, recipe);

        assertNull(recipe.getId());
        assertEquals(request.getTitle(), recipe.getTitle());
        assertEquals(request.getShortDescription(), recipe.getShortDescription());
        assertEquals(request.getThumbnailUrl(), recipe.getThumbnailUrl());
        assertEquals(request.getCoverImageUrl(), recipe.getCoverImageUrl());
        assertEquals(request.getIngredientList().size(), recipe.getIngredientList().size());
        for (int i = 0; i < request.getIngredientList().size(); ++i) {
            assertEquals(request.getIngredientList().get(i).getName(), recipe.getIngredientList().get(i).getName());
            assertEquals(request.getIngredientList().get(i).getQuantity(), recipe.getIngredientList().get(i).getQuantity());
            assertEquals(request.getIngredientList().get(i).getMeasurementUnit(), recipe.getIngredientList().get(i).getMeasurementUnit());
        }
        assertEquals(request.getPreparationInstructions(), recipe.getPreparationInstructions());
        assertEquals(request.getPreparationTimeInMinutes(), recipe.getPreparationTimeInMinutes());
        assertEquals(request.getCookingTimeInMinutes(), recipe.getCookingTimeInMinutes());
        assertEquals(request.getCookingInstructions(), recipe.getCookingInstructions());
        assertNull(recipe.getDateCreated());
        assertNull(recipe.getDateLastUpdated());
        assertEquals(request.getDifficulty(), recipe.getDifficulty());
        assertEquals(request.getCategory(), recipe.getCategory().getCategory());
        assertNull(recipe.getAuthorId());

    }
}
