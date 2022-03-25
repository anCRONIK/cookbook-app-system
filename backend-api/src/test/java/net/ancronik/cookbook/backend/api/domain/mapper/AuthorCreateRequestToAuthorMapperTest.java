package net.ancronik.cookbook.backend.api.domain.mapper;

import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorCreateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@Tag(TestTypes.UNIT)
public class AuthorCreateRequestToAuthorMapperTest {

    AuthorCreateRequestToAuthorMapper mapper = new AuthorCreateRequestToAuthorMapper(new ModelMapper());

    @Test
    public void map_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(null));
    }

    @Test
    public void map_DtoGiven_ReturnPopulatedModel() {
        AuthorCreateRequest request = new AuthorCreateRequest("roki");

        Author author = mapper.map(request);

        assertNotNull(author);
        assertEquals(request.getUsername(), author.getUsername());
        assertNull(author.getDateOfBirth());
        assertNull(author.getImageUrl());
        assertNull(author.getBio());
        assertNull(author.getFullName());
    }
}