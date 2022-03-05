package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
public interface RecipeService {

    /**
     * Method for fetching all recipes with options for paging the data.
     *
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipePreviewDto> getAllRecipes(Pageable pageable);

    /**
     * Method for getting specific recipe using id.
     *
     * @param id recipe id
     * @return optional which will contain recipe if exists
     */
    Optional<RecipeDto> getRecipe(Long id);

    /**
     * Method to find all recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipePreviewDto> getAllRecipesForCategory(String category, Pageable pageable);
}
