package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.validation.annotation.PageableConstraint;
import net.ancronik.cookbook.backend.web.dto.recipe.AddRecipeCommentRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCommentModel;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.RecipeComment}.
 *
 * @author Nikola Presecki
 */
public interface RecipeCommentService {

    /**
     * Method for finding all comments for given recipe.
     *
     * @param id       recipe id, must be positive integer greater than 1
     * @param pageable pageable options, can not be null, also check for max page value because it's going to be validated
     * @return slice with data
     * @throws ConstraintViolationException if data is not inside specified ranges
     */
    Slice<RecipeCommentModel> getCommentsForRecipe(@NonNull @Range(min = 1L) Long id, @NonNull @PageableConstraint Pageable pageable) throws ConstraintViolationException;

    /**
     * Method for adding new comment to the recipe.
     *
     * @param id      recipe id, must be positive integer greater than 1
     * @param comment comment, can't be null and values are going to be validated
     * @throws DataDoesNotExistException    if recipe with given id does not exist in database
     * @throws ConstraintViolationException if data is not inside specified ranges
     */
    @Transactional
    void addCommentToRecipe(@NonNull @Range(min = 1L) Long id, @NonNull @Valid AddRecipeCommentRequest comment) throws DataDoesNotExistException,
            ConstraintViolationException;

}
