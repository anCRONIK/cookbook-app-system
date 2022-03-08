package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Primary key for recipe comment
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class RecipeCommentPK implements Serializable {

    @PrimaryKeyColumn("recipe_id")
    private Long recipeId;

    @PrimaryKeyColumn
    private String username;

    @PrimaryKeyColumn("date_created")
    private LocalDateTime dateCreated;

}
