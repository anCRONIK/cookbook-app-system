package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link AuthorUpdateRequest} to {@link Author}.
 *
 * @author Nikola Presecki
 */
@Component
public class AuthorUpdateRequestToAuthorMapper implements Mapper<AuthorUpdateRequest, Author> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorUpdateRequestToAuthorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Author map(@NonNull AuthorUpdateRequest request) {

        return modelMapper.map(request, Author.class);
    }
}
