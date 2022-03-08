package net.ancronik.cookbook.backend.data.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

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
public class RecipeCategory implements Serializable {

    @PrimaryKey
    private String category;

}
