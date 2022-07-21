package net.ancronik.cookbook.backend.api.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Table("recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe implements Serializable {

    @NotNull
    @PrimaryKey
    private Long id;

    @NotBlank
    @CodePointLength(min = 1, max = 50)
    private String title;

    @CodePointLength(max = 200)
    @Column(value = "short_description")
    private String shortDescription;

    @URL
    @Column(value = "thumbnail_url")
    private String thumbnailUrl;

    @URL
    @Column(value = "cover_image_url")
    private String coverImageUrl;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 1000)
    @Column(value = "ingredients")
    private List<Ingredient> ingredientList;

    @NotNull
    @Range(max = 144000)
    @Column(value = "preparation_time")
    private Integer preparationTimeInMinutes;

    @NotBlank
    @CodePointLength(max = 20000)
    @Column(value = "preparation_instructions")
    private String preparationInstructions;

    @NotNull
    @Range(min = 1, max = 1440)
    @Column(value = "cooking_time")
    private Integer cookingTimeInMinutes;

    @NotBlank
    @CodePointLength(min = 10, max = 100000)
    @Column(value = "cooking_instructions")
    private String cookingInstructions;

    @NotNull
    @Column(value = "date_created")
    private LocalDateTime dateCreated;

    @Column(value = "date_last_updated")
    private LocalDateTime dateLastUpdated;

    @NotNull
    @Range(min = 1, max = 5)
    @Column(value = "difficulty")
    private Integer difficulty;

    @CassandraType(type = CassandraType.Name.VARCHAR)
    @NotNull
    private RecipeCategory category;

    // private Float rating; TODO implement later, need to add additional table to save votes and that should be exposed as another endpoint

    @NotBlank
    @Column(value = "author_username")
    private String authorId;

}
