package net.ancronik.cookbook.backend.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull
    @Size
    @PrimaryKeyColumn("recipe_id")
    private Long recipeId;

    @NotBlank
    @PrimaryKeyColumn
    private String username;

    @NotNull
    @PrimaryKeyColumn("date_created")
    private LocalDateTime dateCreated;

}
