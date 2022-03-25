package net.ancronik.cookbook.backend.api.domain.mapper;

import net.ancronik.cookbook.backend.api.StringTestUtils;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Tag(TestTypes.UNIT)
public class AuthorUpdateRequestAuthorUpdateMapperTest {

    AuthorUpdateRequestAuthorUpdateMapper mapper = new AuthorUpdateRequestAuthorUpdateMapper(new ModelMapper());

    @Test
    public void map_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mapper.update(null, null));
        assertThrows(IllegalArgumentException.class, () -> mapper.update(null, new Author()));
        assertThrows(IllegalArgumentException.class, () -> mapper.update(new AuthorUpdateRequest(), null));
    }

    @Test
    public void map_DtoGiven_ReturnPopulatedModel() {
        AuthorUpdateRequest request = new AuthorUpdateRequest("Roki Novi", LocalDate.of(2020, 2, 2), null, null);

        Author author = new Author();

        mapper.update(request, author);

        assertNotNull(author);
        assertEquals(request.getFullName(), author.getFullName());
        assertEquals(request.getDateOfBirth(), author.getDateOfBirth());
        assertNull(author.getImageUrl());
        assertNull(author.getBio());
        assertNull(author.getUsername());
    }

    @Test
    public void map_DtoGiven_ReturnPopulatedModel2() {
        AuthorUpdateRequest request = new AuthorUpdateRequest(null, null, null, null);

        Author author = new Author();

        mapper.update(request, author);

        assertNull(author.getFullName());
        assertNull(author.getDateOfBirth());
        assertNull(author.getImageUrl());
        assertNull(author.getBio());
        assertNull(author.getUsername());
    }

    @Test
    public void map_DtoGiven_ReturnPopulatedModel3() {
        AuthorUpdateRequest request = new AuthorUpdateRequest("Roki Novi", LocalDate.of(2020, 2, 2), "Nothing special", StringTestUtils.generateRandomUrl());

        Author author = new Author();
        author.setUsername("test");

        mapper.update(request, author);

        assertNotNull(author);
        assertEquals("test", author.getUsername());
        assertEquals(request.getFullName(), author.getFullName());
        assertEquals(request.getDateOfBirth(), author.getDateOfBirth());
        assertEquals(request.getImageUrl(), author.getImageUrl());
        assertEquals(request.getBio(), author.getBio());
    }
}