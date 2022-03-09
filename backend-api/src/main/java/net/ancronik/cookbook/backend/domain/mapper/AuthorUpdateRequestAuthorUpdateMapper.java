package net.ancronik.cookbook.backend.domain.mapper;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper updating  {@link Author} with data from {@link AuthorUpdateRequest}.
 *
 * @author Nikola Presecki
 */
@Component
public class AuthorUpdateRequestAuthorUpdateMapper implements UpdateMapper<AuthorUpdateRequest, Author> {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorUpdateRequestAuthorUpdateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void update(@NonNull AuthorUpdateRequest authorUpdateRequest, @NonNull Author author) {
        modelMapper.map(authorUpdateRequest, author);
    }
}
