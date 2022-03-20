package net.ancronik.cookbook.backend.api.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ancronik.cookbook.backend.api.data.model.Ingredient;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO representing {@link Ingredient}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @NotNull
    @CodePointLength(min = 2, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 6)
    @Pattern(regexp = "^\\d+[/.]?\\d*$")
    private String quantity;

    @Size(max = 8)
    private String measurementUnit;

}
