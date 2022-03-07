package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.dto.author.AuthorDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class AuthorDtoAssemblerTest {

    AuthorDtoAssembler assembler = new AuthorDtoAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }


    @Test
    public void toModel_ValidDataGiven_ValidDtoReturned() {
        Author author = new Author("roki", "Roki Pazi", LocalDate.of(1999, 12, 1), "Najbolji kuhar ikad", null);

        AuthorDto authorDto = assembler.toModel(author);
        assertEquals("AuthorDto(username=roki, fullName=Roki Pazi, dateOfBirth=1999-12-01, bio=Najbolji kuhar ikad, imageUrl=null)", authorDto.toString());

        author.setImageUrl(StringTestUtils.generateRandomUrl());
        assertEquals("AuthorDto(username=roki, fullName=Roki Pazi, dateOfBirth=1999-12-01, bio=Najbolji kuhar ikad, imageUrl=" + author.getImageUrl() + ")", assembler.toModel(author).toString());
    }

}
