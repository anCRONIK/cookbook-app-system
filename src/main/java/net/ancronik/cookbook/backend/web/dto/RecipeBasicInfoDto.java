package net.ancronik.cookbook.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

/**
 * DTO representing preview of {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "recipes", itemRelation = "recipe")
@JsonRootName(value = "recipe")
public class RecipeBasicInfoDto extends RepresentationModel<RecipeBasicInfoDto> {

    private Long id;

    private String title;

    private String shortDescription;

    private String coverImageUrl;

    private LocalDateTime dateCreated;

    private Integer difficulty;

    private String category;

    private String authorId;

}
