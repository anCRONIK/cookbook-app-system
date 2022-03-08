package net.ancronik.cookbook.backend.data.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Model representing recipe category. This is read-only entity.
 *
 * @author Nikola Presecki
 */
@Table("recipe_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RecipeCategory implements Serializable {

    @PrimaryKey
    @NotBlank
    @Size(max = 20)
    private String category;

}
