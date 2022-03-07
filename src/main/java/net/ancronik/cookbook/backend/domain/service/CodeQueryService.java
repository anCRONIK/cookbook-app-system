package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitDto;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCategoryDto;

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
    List<MeasurementUnitDto> getMeasurementUnits() throws GenericDatabaseException;

    /**
     * Method for checking if given measurement unit code is valid.
     *
     * @param unit unit
     * @return {@literal true} if valid, otherwise {@literal false}
     */
    boolean isMeasurementUnitValid(String unit) throws GenericDatabaseException;

    /**
     * Method for fetching all {@link RecipeCategory} as string;
     *
     * @return list of recipe categories
     */
    List<RecipeCategoryDto> getRecipeCategories() throws GenericDatabaseException;

    /**
     * Method for checking if recipe category is valid.
     *
     * @param category category
     * @return {@literal true} if valid, otherwise {@literal false}
     */
    boolean isRecipeCategoryValid(String category) throws GenericDatabaseException;
}
