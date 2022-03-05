package net.ancronik.cookbook.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

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
public class RecipePreviewDto extends EntityModel<RecipePreviewDto> {

    private Long id;

    private String title;

    private String shortDescription;

    private String coverImageUrl;

    private LocalDateTime dateCreated;

    private Integer difficulty;

    private String category;

    private String authorId;

}
