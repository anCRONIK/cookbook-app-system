package net.ancronik.cookbook.backend.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table("recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @PrimaryKey
    private Integer id;

    private List<Ingredient> ingredientList;

}
