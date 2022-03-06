package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExist;
import net.ancronik.cookbook.backend.web.dto.AddCommentRequest;
import net.ancronik.cookbook.backend.web.dto.RecipeCommentDto;
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
     * @throws DataDoesNotExist if recipe with given id does not exist in database
     */
    Slice<RecipeCommentDto> getCommentsForRecipe(Long id, Pageable pageable) throws DataDoesNotExist;

    /**
     * Method for adding new comment to the recipe.
     *
     * @param id   recipe id
     * @param text comment
     * @throws DataDoesNotExist if recipe with given id does not exist in database
     */
    void addCommentToRecipe(Long id, AddCommentRequest text) throws DataDoesNotExist;

}
