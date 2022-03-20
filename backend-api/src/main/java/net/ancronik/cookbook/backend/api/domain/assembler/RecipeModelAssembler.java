package net.ancronik.cookbook.backend.api.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.web.controller.AuthorController;
import net.ancronik.cookbook.backend.api.web.controller.RecipeController;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for {@link RecipeModel} from {@link Recipe}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeModelAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeModelAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipeModel.class);
        this.modelMapper = modelMapper;
    }

    @SneakyThrows
    @Override
    public RecipeModel toModel(@NonNull Recipe entity) {
        RecipeModel dto = modelMapper.map(entity, RecipeModel.class);

        dto.add(linkTo(methodOn(RecipeController.class).getRecipeById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(RecipeController.class).getRecipesForCategory(dto.getCategory(), null)).withRel("search_category"));
        dto.add(linkTo(methodOn(AuthorController.class).getAuthorById(dto.getAuthorId())).withRel("author"));

        return dto;
    }

}
