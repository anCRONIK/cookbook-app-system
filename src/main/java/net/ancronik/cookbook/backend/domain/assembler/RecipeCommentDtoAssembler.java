package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.RecipeComment;
import net.ancronik.cookbook.backend.web.controller.RecipeController;
import net.ancronik.cookbook.backend.web.dto.RecipeCommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Assembler for {@link net.ancronik.cookbook.backend.web.dto.RecipeCommentDto} from {@link net.ancronik.cookbook.backend.data.model.RecipeComment}.
 *
 * @author Nikola Presecki
 */
@Component
public class RecipeCommentDtoAssembler extends RepresentationModelAssemblerSupport<RecipeComment, RecipeCommentDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeCommentDtoAssembler(ModelMapper modelMapper) {
        super(RecipeController.class, RecipeCommentDto.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeCommentDto toModel(@NonNull RecipeComment entity) {
        RecipeCommentDto dto = modelMapper.map(Objects.requireNonNull(entity, "Given recipe comment entity is null"), RecipeCommentDto.class);

        //TODO link to author

        return dto;
    }


}
