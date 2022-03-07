package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.web.controller.AuthorController;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

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
    @SneakyThrows
    public RecipeBasicInfoDto toModel(@NonNull Recipe entity) {
        RecipeBasicInfoDto dto = modelMapper.map(entity, RecipeBasicInfoDto.class);

        dto.add(linkTo(methodOn(RecipeController.class).getRecipeById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(RecipeController.class).getRecipesForCategory(dto.getCategory(), null)).withRel("search_category"));
        dto.add(linkTo(methodOn(AuthorController.class).getAuthorById(dto.getAuthorId())).withRel("author"));

        return dto;
    }


}
