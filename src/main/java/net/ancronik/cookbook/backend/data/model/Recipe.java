package net.ancronik.cookbook.backend.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.ZonedDateTime;
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
    private Long id;
    //TODO limit check, not null or empty
    private String title;
    //TODO limit to 200, can be null and empty
    @Column(value = "short_description")
    private String shortDescription;
    //TODO can be null or empty, url validation
    @Column(value = "cover_image_url")
    private String coverImageUrl;
    //TODO can not be empty or null
    @Column(value = "ingredients")
    private List<Ingredient> ingredientList;
    //TODO only positive values
    @Column(value = "preparation_time")
    private Integer preparationTime;
    //TODO limit
    @Column(value = "preparation_instructions")
    private String preparationInstructions;
    //TODO only positive values
    @Column(value = "cooking_time")
    private Integer cookingTime;
    //TODO limit
    @Column(value = "cooking_instructions")
    private String cookingInstructions;
    //TODO not null
    @Column(value = "date_created")
    private ZonedDateTime dateCreated;
    //TODO checks
    @Column(value = "date_last_updated")
    private ZonedDateTime dateLastUpdated;
    @Column(value = "difficulty")
    private RecipeDifficulty difficulty;

    private RecipeCategory category;

    private Float rating;
    //TODO not null
    @Column(value = "author_username")
    private String authorId;

}
