package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import net.ancronik.cookbook.backend.web.dto.RecipeCommentDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
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
     * Method for fetching recipes with options for pagination.
     *
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeBasicInfoDto> getRecipes(Pageable pageable);

    /**
     * Method for getting specific recipe using id.
     *
     * @param id recipe id
     * @return optional which will contain recipe if exists
     */
    Optional<RecipeDto> getRecipe(Long id);

    /**
     * Method to find recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeBasicInfoDto> getRecipesForCategory(String category, Pageable pageable);

    /**
     * Method for finding all comments for given recipe.
     *
     * @param id       recipe id
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeCommentDto> getCommentsForRecipe(Long id, Pageable pageable);
}
