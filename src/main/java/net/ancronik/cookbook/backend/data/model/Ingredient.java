package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    //TODO limit size etc
    private String name;

    private IngredientQuantity quantity;

}
