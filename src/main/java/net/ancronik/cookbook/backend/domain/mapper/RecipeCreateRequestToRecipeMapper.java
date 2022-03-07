package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
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
        recipe.setCategory(new RecipeCategory(request.getCategory()));

        return recipe;
    }
}
