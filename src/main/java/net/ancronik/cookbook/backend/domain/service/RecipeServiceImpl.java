package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link net.ancronik.cookbook.backend.domain.service.RecipeService}.
 *
 * @author Nikola Presecki
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Override
    public Page<RecipeDto> getAllRecipes(int page, int size, Sort sort) {

        return null;
    }
}
