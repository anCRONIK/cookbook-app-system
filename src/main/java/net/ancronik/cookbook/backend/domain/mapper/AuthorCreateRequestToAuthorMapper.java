package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for {@link AuthorCreateRequest} to {@link Author}.
 *
 * @author Nikola Presecki
 */
@Component
public class AuthorCreateRequestToAuthorMapper implements Mapper<AuthorCreateRequest, Author> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorCreateRequestToAuthorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Author map(@NonNull AuthorCreateRequest request) {

        return modelMapper.map(request, Author.class);
    }
}
