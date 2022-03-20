package net.ancronik.cookbook.backend.api.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.data.model.RecipeComment;
import net.ancronik.cookbook.backend.api.web.controller.AuthorController;
import net.ancronik.cookbook.backend.api.web.controller.RecipeController;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCommentModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for {@link RecipeCommentModel} from {@link RecipeComment}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeCommentModelAssembler extends RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeCommentModelAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipeCommentModel.class);
        this.modelMapper = modelMapper;
    }

    @SneakyThrows
    @Override
    public RecipeCommentModel toModel(@NonNull RecipeComment entity) {
        RecipeCommentModel dto = modelMapper.map(entity, RecipeCommentModel.class);
        dto.setDateCreated(entity.getRecipeCommentPK().getDateCreated());
        dto.setUsername(entity.getRecipeCommentPK().getUsername());

        dto.add(linkTo(methodOn(AuthorController.class).getAuthorById(dto.getUsername())).withRel("author"));

        return dto;
    }


}
