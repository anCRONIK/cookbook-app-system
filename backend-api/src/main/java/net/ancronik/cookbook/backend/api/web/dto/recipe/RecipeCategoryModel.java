package net.ancronik.cookbook.backend.api.web.dto.recipe;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "categories", itemRelation = "category")
@JsonRootName(value = "category")
public class RecipeCategoryModel {

    private String category;

}
