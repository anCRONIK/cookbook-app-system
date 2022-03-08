package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Assembler for {@link AuthorModel} from {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
@Component
public class AuthorModelAssembler extends RepresentationModelAssemblerSupport<Author, AuthorModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorModelAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, AuthorModel.class);
        this.modelMapper = modelMapper;
    }

    @SneakyThrows
    @Override
    public AuthorModel toModel(@NonNull Author entity) {

        return modelMapper.map(entity, AuthorModel.class);
    }

}
