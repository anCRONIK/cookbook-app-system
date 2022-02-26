package net.ancronik.cookbook.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * DTO representing {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto extends RepresentationModel<RecipeDto> {

    private Integer id;

    private String title;

    private String shortDescription;

    private String coverImageUrl;

    private List<IngredientDto> ingredientList;

    private Integer preparationTime;

    private String preparationInstructions;

    private Integer cookTime;

    private String cookingInstructions;

    private Integer difficulty;
}
