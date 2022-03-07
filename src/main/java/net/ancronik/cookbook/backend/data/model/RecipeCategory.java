package net.ancronik.cookbook.backend.data.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * Model representing recipe category.
 *
 * @author Nikola Presecki
 */
@Table("recipe_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RecipeCategory {

    private String category;

}
