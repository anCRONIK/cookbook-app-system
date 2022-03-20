package net.ancronik.cookbook.backend.api.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotBlank;

/**
 * Request for creating new {@link RecipeComment}.
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
