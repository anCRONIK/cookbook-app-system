package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.author.AuthorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Assembler for {@link AuthorDto} from {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
@Component
public class AuthorDtoAssembler extends RepresentationModelAssemblerSupport<Author, AuthorDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorDtoAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, AuthorDto.class);
        this.modelMapper = modelMapper;
    }

    @SneakyThrows
    @Override
    public AuthorDto toModel(@NonNull Author entity) {

        return modelMapper.map(entity, AuthorDto.class);
    }

}
