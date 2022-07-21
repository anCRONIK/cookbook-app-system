package net.ancronik.cookbook.backend.api.domain.mapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RecipeMapper {

    private final ModelMapper modelMapper;

    public Recipe map(@NonNull RecipeCreateRequest request) {
        Recipe recipe = modelMapper.map(request, Recipe.class);
        recipe.setCategory(new RecipeCategory(request.getCategory()));

        return recipe;
    }

    public void update(@NonNull RecipeUpdateRequest request, @NonNull Recipe recipe) {
        modelMapper.map(request, recipe);
        recipe.setCategory(new RecipeCategory(request.getCategory()));
    }

}
