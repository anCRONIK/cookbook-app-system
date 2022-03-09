package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link RecipeUpdateRequest} to {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeUpdateRequestRecipeUpdateMapper implements UpdateMapper<RecipeUpdateRequest, Recipe> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeUpdateRequestRecipeUpdateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void update(@NonNull RecipeUpdateRequest request, @NonNull Recipe recipe) {
        modelMapper.map(request, recipe);
        recipe.setCategory(new RecipeCategory(request.getCategory()));
    }
}
