package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.ZonedDateTime;

/**
 * Database model for comments that user's leave on the recipes.
 *
 * @author Nikola Presecki
 */
@Table("recipe_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeComments {

    //TODO checks
    @Column("recipe_id")
    private Integer recipeId;
    //TODO checks
    private String username;
    //TODO checks
    private String text;
    //TODO checks
    @Column("date_created")
    private ZonedDateTime dateCreated;

}
