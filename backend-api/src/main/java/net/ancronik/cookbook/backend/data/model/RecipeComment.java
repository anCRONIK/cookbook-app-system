package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

/**
 * Database model for comments that users leave on the recipes.
 *
 * @author Nikola Presecki
 */
@Table("recipe_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeComment implements Serializable {

    @PrimaryKey
    RecipeCommentPK recipeCommentPK;

    private String text;

}
