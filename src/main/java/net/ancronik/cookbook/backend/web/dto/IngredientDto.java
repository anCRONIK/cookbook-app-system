package net.ancronik.cookbook.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing {@link net.ancronik.cookbook.backend.data.model.Ingredient}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    private String name;

    private String quantity;

    private String measurementUnit;

}
