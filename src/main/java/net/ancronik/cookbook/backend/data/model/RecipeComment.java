package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Database model for comments that users leave on the recipes.
 *
 * @author Nikola Presecki
 */
@Table("recipe_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeComment {

    //TODO checks
    @Column("recipe_id")
    private Long recipeId;
    //TODO checks
    private String username;
    //TODO checks
    private String text;
    //TODO checks
    @Column("date_created")
    private LocalDateTime dateCreated;

}
