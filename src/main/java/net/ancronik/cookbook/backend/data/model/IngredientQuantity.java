package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Class representing ingredient quantity.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientQuantity {

    //TODO need to check if it is a number because we should allow entries like (3/4 CUP)
    private String quantity;

    private MeasurementUnit measurementUnit;

}
