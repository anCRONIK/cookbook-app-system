package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for {@link RecipePreviewDto} from {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipePreviewDtoAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipePreviewDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipePreviewDtoAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipePreviewDto.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipePreviewDto toModel(@NonNull Recipe entity) {
        RecipePreviewDto dto = modelMapper.map(entity, RecipePreviewDto.class);

        dto.add(linkTo(methodOn(RecipeController.class).findRecipeById(dto.getId(), null)).withSelfRel());
        dto.add(linkTo(methodOn(RecipeController.class).getAllRecipesForCategory(dto.getCategory(), null)).withRel("search_category"));
        //TODO link to author

        return dto;
    }


}
