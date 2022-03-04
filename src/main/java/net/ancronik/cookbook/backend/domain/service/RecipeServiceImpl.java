package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public Slice<RecipeDto> getAllRecipes(Pageable pageable) {

        return null;
    }
}
