package net.ancronik.cookbook.backend.web.dto.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "recipe")
public class RecipeDto extends RepresentationModel<RecipeDto> {

    private Long id;

    private String title;

    private String shortDescription;

    private String coverImageUrl;

    @JsonProperty("ingredients")
    private List<IngredientDto> ingredientList;

    private Integer preparationTime;

    private String preparationInstructions;

    private Integer cookingTime;

    private String cookingInstructions;

    private LocalDateTime dateCreated;

    private LocalDateTime lastUpdated;

    private Integer difficulty;

    private String category;

    private String authorId;
}
