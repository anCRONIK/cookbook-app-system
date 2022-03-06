package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExist;
import net.ancronik.cookbook.backend.web.dto.CreateRecipeRequest;
import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.UpdateRecipeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

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
     * @return which will contain recipe if exists
     * @throws DataDoesNotExist if recipe with given id does not exist in database
     */
    RecipeDto getRecipe(Long id) throws DataDoesNotExist;

    /**
     * Method to find recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeBasicInfoDto> getRecipesForCategory(String category, Pageable pageable);

    /**
     * Method for creating new recipe.
     *
     * @param request create request
     * @return newly created recipe
     */
    RecipeDto createRecipe(CreateRecipeRequest request);

    /**
     * Method for deleting specific recipe using id.
     *
     * @param id recipe id
     * @throws DataDoesNotExist if recipe with given id does not exist in database
     */
    void deleteRecipe(Long id) throws DataDoesNotExist;

    /**
     * Method for updating specific recipe using id.
     *
     * @param id      recipe id
     * @param request update request
     * @return updated recipe
     * @throws DataDoesNotExist if recipe with given id does not exist in database
     */
    RecipeDto updateRecipe(Long id, UpdateRecipeRequest request) throws DataDoesNotExist;
}
