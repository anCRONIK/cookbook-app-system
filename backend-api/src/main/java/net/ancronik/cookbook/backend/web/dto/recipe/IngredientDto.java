package net.ancronik.cookbook.backend.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO representing {@link net.ancronik.cookbook.backend.data.model.Ingredient}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @CodePointLength(min = 2, max = 100)
    private String name;

    @Size(min = 1, max = 6)
    @Pattern(regexp = "^\\d+[/.]?\\d*$")
    private String quantity;

    @Size(min = 1, max = 8)
    private String measurementUnit;

}
