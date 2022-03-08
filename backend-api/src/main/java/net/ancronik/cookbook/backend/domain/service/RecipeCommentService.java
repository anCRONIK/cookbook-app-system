package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.recipe.AddRecipeCommentRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.RecipeComment}.
 *
 * @author Nikola Presecki
 */
public interface RecipeCommentService {

    /**
     * Method for finding all comments for given recipe.
     *
     * @param id       recipe id
     * @param pageable pageable options
     * @return slice with data
     * @throws DataDoesNotExistException if recipe with given id does not exist in database
     */
    Slice<RecipeCommentDto> getCommentsForRecipe(@NonNull Long id, Pageable pageable) throws DataDoesNotExistException;

    /**
     * Method for adding new comment to the recipe.
     *
     * @param id      recipe id
     * @param comment comment
     * @throws DataDoesNotExistException     if recipe with given id does not exist in database
     * @throws IllegalDataInRequestException if request is not valid
     */
    void addCommentToRecipe(@NonNull Long id, @NonNull AddRecipeCommentRequest comment) throws DataDoesNotExistException, IllegalDataInRequestException;

}
