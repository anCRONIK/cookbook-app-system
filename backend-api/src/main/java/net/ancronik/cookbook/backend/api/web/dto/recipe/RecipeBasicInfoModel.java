package net.ancronik.cookbook.backend.api.web.dto.recipe;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

/**
 * DTO representing preview of {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "recipes", itemRelation = "recipe")
@JsonRootName(value = "recipe")
public class RecipeBasicInfoModel extends RepresentationModel<RecipeBasicInfoModel> {

    private Long id;

    private String title;

    private String shortDescription;

    private String thumbnailUrl;

    private LocalDateTime dateCreated;

    private Integer preparationTimeInMinutes;

    private Integer cookingTimeInMinutes;

    private Integer difficulty;

    private String category;

    private String authorId;

}
