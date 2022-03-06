package net.ancronik.cookbook.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

/**
 * Dto for comments that users leave on the recipes.
 *
 * @author Nikola Presecki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "comments", itemRelation = "comment")
@JsonRootName(value = "comment")
public class RecipeCommentDto extends RepresentationModel<RecipeCommentDto> {

    private String username;

    private String text;

    private LocalDateTime dateCreated;

}
