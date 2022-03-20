package net.ancronik.cookbook.backend.api.web.dto.recipe;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Request for creating new {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeUpdateRequest {

    @NotBlank
    @CodePointLength(min = 1, max = 50)
    private String title;

    @CodePointLength(max = 200)
    private String shortDescription;

    @URL
    private String thumbnailUrl;

    @URL
    private String coverImageUrl;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 1000)
    @JsonAlias("ingredients")
    private List<IngredientDto> ingredientList;

    @NotNull
    @Range(max = 144000)
    private Integer preparationTimeInMinutes;

    @NotBlank
    @CodePointLength(max = 20000)
    private String preparationInstructions;

    @NotNull
    @Range(min = 1, max = 1440)
    private Integer cookingTimeInMinutes;

    @NotBlank
    @CodePointLength(min = 10, max = 100000)
    private String cookingInstructions;

    @Range(min = 1, max = 5)
    private Integer difficulty;

    @NotBlank
    @Size(min = 1, max = 50)
    private String category;
}
