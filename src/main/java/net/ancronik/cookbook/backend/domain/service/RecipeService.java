package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
public interface RecipeService {

    /**
     * Method for fetching all recipes with options for paging the data.
     *
     * @param page page number which is required
     * @param size page size
     * @param sort sort options
     * @return page with data
     */
    Page<RecipeDto> getAllRecipes(int page, int size, Sort sort);

}
