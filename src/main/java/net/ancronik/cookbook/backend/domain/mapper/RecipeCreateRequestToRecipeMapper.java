package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.model.RecipeDifficulty;
import net.ancronik.cookbook.backend.web.dto.RecipeCreateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link RecipeCreateRequest} yo {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeCreateRequestToRecipeMapper implements Mapper<RecipeCreateRequest, Recipe> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeCreateRequestToRecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Recipe map(@NonNull RecipeCreateRequest request) {
        Recipe recipe = modelMapper.map(request, Recipe.class);
        for (int i = 0; i < request.getIngredientList().size(); ++i) { //FIXME try to modify mapper for automatic mapping or remove enum?
            recipe.getIngredientList().get(i).getQuantity().setMeasurementUnit(MeasurementUnit.valueOf(request.getIngredientList().get(i).getMeasurementUnit()));
        }
        recipe.setDifficulty(RecipeDifficulty.parse(request.getDifficulty()));

        return recipe;
    }
}
