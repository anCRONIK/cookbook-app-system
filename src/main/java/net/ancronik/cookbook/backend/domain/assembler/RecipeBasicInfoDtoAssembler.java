package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for {@link RecipeBasicInfoDto} from {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeBasicInfoDtoAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeBasicInfoDtoAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipeBasicInfoDto.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeBasicInfoDto toModel(@NonNull Recipe entity) {
        RecipeBasicInfoDto dto = modelMapper.map(Objects.requireNonNull(entity, "Given recipe entity is null"), RecipeBasicInfoDto.class);

        dto.add(linkTo(methodOn(RecipeController.class).findRecipeById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(RecipeController.class).getRecipesForCategory(dto.getCategory(), null)).withRel("search_category"));
        //TODO link to author

        return dto;
    }


}
