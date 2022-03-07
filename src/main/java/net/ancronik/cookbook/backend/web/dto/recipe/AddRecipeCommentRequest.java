package net.ancronik.cookbook.backend.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for creating new {@link net.ancronik.cookbook.backend.data.model.RecipeComment}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeCommentRequest {

    private String text;

}