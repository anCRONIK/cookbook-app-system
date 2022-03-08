package net.ancronik.cookbook.backend.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotBlank;

/**
 * Request for creating new {@link net.ancronik.cookbook.backend.data.model.RecipeComment}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeCommentRequest {

    @NotBlank
    @CodePointLength(max = 10000)
    private String text;

}
