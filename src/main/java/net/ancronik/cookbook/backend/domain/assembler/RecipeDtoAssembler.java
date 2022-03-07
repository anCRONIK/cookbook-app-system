package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.data.model.Ingredient;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.IngredientDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for {@link RecipeDto} from {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeDtoAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeDtoAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipeDto.class);
        this.modelMapper = modelMapper;
    }

    @SneakyThrows
    @Override
    public RecipeDto toModel(@NonNull Recipe entity) {
        RecipeDto dto = modelMapper.map(Objects.requireNonNull(entity, "Given recipe entity is null"), RecipeDto.class);

        dto.add(linkTo(methodOn(RecipeController.class).findRecipeById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(RecipeController.class).getRecipesForCategory(dto.getCategory(), null)).withRel("search_category"));
        //TODO link to author

        return dto;
    }


    private IngredientDto toDto(Ingredient entity) {
        return modelMapper.map(entity, IngredientDto.class);
    }


}
