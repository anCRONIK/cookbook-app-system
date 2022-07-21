package net.ancronik.cookbook.backend.api.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.application.exceptions.UnauthorizedActionException;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.validation.annotation.PageableConstraint;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeUpdateRequest;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Service handling all operations for the {@link Recipe}.
 *
 * @author Nikola Presecki
 */
public interface RecipeService {

    /**
     * Method for fetching recipes with options for pagination.
     *
     * @param pageable pageable options, if null default pageable will be used
     * @return slice with data
     * @throws ConstraintViolationException if pageable is unpaged or max size is exceeded
     */
    Slice<RecipeBasicInfoModel> getRecipes(@NonNull @PageableConstraint Pageable pageable) throws ConstraintViolationException;

    /**
     * Method for getting specific recipe using id.
     *
     * @param id recipe id, can't be null and must be greater than 1
     * @return which will contain recipe if exists
     * @throws DataDoesNotExistException    if recipe with given id does not exist in database
     * @throws ConstraintViolationException if id is not in valid range
     */
    RecipeModel getRecipe(@NonNull @NotNull @Range(min = 1) Long id) throws DataDoesNotExistException, ConstraintViolationException;

    /**
     * Method to find recipes in the given category
     *
     * @param category category
     * @param pageable pageable options
     * @return slice with data
     * @throws ConstraintViolationException if category is not in valid range or pageable is unpaged or max size is exceeded
     */
    Slice<RecipeBasicInfoModel> getRecipesForCategory(@NonNull @NotBlank @Size(min = 1, max = 50) String category, @NonNull @PageableConstraint Pageable pageable)
        throws ConstraintViolationException;

    /**
     * Method for creating new recipe.
     *
     * @param request create request, can not be null and value will be validated
     * @return newly created recipe
     * @throws ConstraintViolationException if request is not valid
     */
    @Transactional
    RecipeModel createRecipe(@NonNull @NotNull @Valid RecipeCreateRequest request) throws ConstraintViolationException;

    /**
     * Method for deleting specific recipe using id.
     *
     * @param id recipe id, can't be null and must be greater than 1
     * @throws DataDoesNotExistException    if recipe with given id does not exist in database
     * @throws ConstraintViolationException if id is not in valid range
     * @throws UnauthorizedActionException  if user is not the author of given recipe
     */
    @Transactional
    void deleteRecipe(@NonNull @NotNull @Range(min = 1) Long id) throws DataDoesNotExistException, ConstraintViolationException, UnauthorizedActionException;

    /**
     * Method for updating specific recipe using id.
     *
     * @param id      recipe id
     * @param request update request
     * @return updated recipe
     * @throws DataDoesNotExistException    if recipe with given id does not exist in database
     * @throws ConstraintViolationException if id is not in valid range or request is not valid
     * @throws UnauthorizedActionException  if user is not the author of given recipe
     */
    @Transactional
    RecipeModel updateRecipe(@NonNull @NotNull @Range(min = 1) Long id, @NonNull @NotNull @Valid RecipeUpdateRequest request)
        throws DataDoesNotExistException, ConstraintViolationException, UnauthorizedActionException;
}
