package net.ancronik.cookbook.backend.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

/**
 * Database model for recipe.
 *
 * @author Nikola Presecki
 */
@Table("recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @PrimaryKey
    private Integer id;
    //TODO limit check, not null or empty
    private String title;
    //TODO limit to 200, can be null and empty
    private String shortDescription;
    //TODO can be null or empty, url validation
    private String coverImageUrl;
    //TODO can not be empty or null
    private List<Ingredient> ingredientList;
    //TODO only positive values
    private Integer preparationTime;
    //TODO limit
    private String preparationInstructions;
    //TODO only positive values
    private Integer cookTime;
    //TODO limit
    private String cookingInstructions;

    private RecipeDifficulty difficulty;
    // ManyToOne relationship
    private Author author;

}
