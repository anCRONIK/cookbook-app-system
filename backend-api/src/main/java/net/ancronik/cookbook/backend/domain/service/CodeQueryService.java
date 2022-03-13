package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCategoryModel;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Service which should handle fetching all codes from db and caching them properly.
 *
 * @author Nikola Presecki
 */
public interface CodeQueryService {

    /**
     * Method for fetching all {@link MeasurementUnit} as dto
     *
     * @return list of units
     */
    List<MeasurementUnitModel> getMeasurementUnits();

    /**
     * Method for checking if given measurement unit code is valid.
     *
     * @param unit unit, can not be null, but can be emtpy
     * @return {@literal true} if valid, otherwise {@literal false}
     */
    boolean isMeasurementUnitValid(@NonNull String unit);

    /**
     * Method for fetching all {@link RecipeCategory} as string;
     *
     * @return list of recipe categories
     */
    List<RecipeCategoryModel> getRecipeCategories();

    /**
     * Method for checking if recipe category is valid.
     *
     * @param category category, can not be null or empty
     * @return {@literal true} if valid, otherwise {@literal false}
     */
    boolean isRecipeCategoryValid(@NonNull @NotBlank String category);
}
