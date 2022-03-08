package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class representing recipe ingredient.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {

    //TODO limit size etc
    private String name;

    //TODO need to check if it is a number because we should allow entries like (3/4 CUP)
    private String quantity;

    private String measurementUnit;

}
