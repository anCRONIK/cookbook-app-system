package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
public interface RecipeService {

    /**
     * Method for fetching recipes with options for pagination.
     *
     * @param pageable pageable options, if null default pageable will be used
     * @return slice with data
     */
    Slice<RecipeBasicInfoModel> getRecipes(@PageableDefault Pageable pageable);

    /**
     * Method for getting specific recipe using id.
     *
     * @param id recipe id, can't be null and must be greater than 1
     * @return which will contain recipe if exists
     * @throws DataDoesNotExistException if recipe with given id does not exist in database
     */
    RecipeModel getRecipe(@NotNull @Range(min = 1) Long id) throws DataDoesNotExistException;

    /**
     * Method to find recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     */
    Slice<RecipeBasicInfoModel> getRecipesForCategory(@NotNull @Size(min = 1, max = 50) String category, @PageableDefault Pageable pageable);

    /**
     * Method for creating new recipe.
     *
     * @param request create request, can not be null and value will be validated
     * @return newly created recipe
     * @throws IllegalDataInRequestException if request is not properly populated
     */
    @Transactional
    RecipeModel createRecipe(@NotNull @Valid RecipeCreateRequest request) throws IllegalDataInRequestException;

    /**
     * Method for deleting specific recipe using id.
     *
     * @param id recipe id, can't be null and must be greater than 1
     * @throws DataDoesNotExistException if recipe with given id does not exist in database
     */
    @Transactional
    void deleteRecipe(@NotNull @Range(min = 1) Long id) throws DataDoesNotExistException;

    /**
     * Method for updating specific recipe using id.
     *
     * @param id      recipe id
     * @param request update request
     * @return updated recipe
     * @throws DataDoesNotExistException     if recipe with given id does not exist in database
     * @throws IllegalDataInRequestException if request is not properly populated
     */
    @Transactional
    RecipeModel updateRecipe(@NotNull @Range(min = 1) Long id, @NotNull @Valid RecipeUpdateRequest request) throws DataDoesNotExistException,
            IllegalDataInRequestException;
}
