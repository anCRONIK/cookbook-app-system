package net.ancronik.cookbook.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * DTO representing {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;

    private String title;

    private String shortDescription;

    private String coverImageUrl;

    private List<IngredientDto> ingredientList;

    private Integer preparationTime;

    private String preparationInstructions;

    private Integer cookTime;

    private String cookingInstructions;

    private ZonedDateTime dateCreated;

    private ZonedDateTime lastUpdated;

    private Integer difficulty;

    private String category;

    private Float rating;

    private String authorId;
}
