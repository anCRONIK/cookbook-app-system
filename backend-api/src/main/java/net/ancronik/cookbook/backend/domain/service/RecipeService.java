package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
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
    Slice<RecipeBasicInfoModel> getRecipes(Pageable pageable);

    /**
     * Method for getting specific recipe using id.
     *
     * @param id recipe id
     * @return which will contain recipe if exists
     * @throws DataDoesNotExistException if recipe with given id does not exist in database
     */
    RecipeModel getRecipe(@NonNull Long id) throws DataDoesNotExistException;

    /**
     * Method to find recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeBasicInfoModel> getRecipesForCategory(@NonNull String category, Pageable pageable);

    /**
     * Method for creating new recipe.
     *
     * @param request create request
     * @return newly created recipe
     * @throws IllegalDataInRequestException if request is not properly populated
     */
    RecipeModel createRecipe(@NonNull RecipeCreateRequest request) throws IllegalDataInRequestException;

    /**
     * Method for deleting specific recipe using id.
     *
     * @param id recipe id
     * @throws DataDoesNotExistException if recipe with given id does not exist in database
     */
    void deleteRecipe(@NonNull Long id) throws DataDoesNotExistException;

    /**
     * Method for updating specific recipe using id.
     *
     * @param id      recipe id
     * @param request update request
     * @return updated recipe
     * @throws DataDoesNotExistException     if recipe with given id does not exist in database
     * @throws IllegalDataInRequestException if request is not properly populated
     */
    RecipeModel updateRecipe(@NonNull Long id, @NonNull RecipeUpdateRequest request) throws DataDoesNotExistException, IllegalDataInRequestException;
}
