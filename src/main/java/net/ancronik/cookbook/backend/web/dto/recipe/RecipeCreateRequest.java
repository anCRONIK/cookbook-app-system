package net.ancronik.cookbook.backend.web.dto.recipe;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request for creating new {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateRequest {

    private String title;

    private String shortDescription;

    private String thumbnailUrl;

    private String coverImageUrl;

    @JsonAlias("ingredients")
    private List<IngredientDto> ingredientList;

    private Integer preparationTime;

    private String preparationInstructions;

    private Integer cookingTime;

    private String cookingInstructions;

    private Integer difficulty;

    private String category;
}
