package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link net.ancronik.cookbook.backend.domain.service.RecipeService}.
 *
 * @author Nikola Presecki
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Override
    public Slice<RecipeDto> getAllRecipes(Pageable pageable) {

        return null;
    }

    @Override
    public Optional<RecipeDto> getRecipe(Long id) {
        return Optional.empty();
    }

    @Override
    public Slice<RecipeDto> getAllRecipesForCategory(String category, Pageable pageable) {
        return null;
    }
}
