package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class AuthorModelAssemblerTest {

    AuthorModelAssembler assembler = new AuthorModelAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }


    @Test
    public void toModel_ValidDataGiven_ValidDtoReturned() {
        Author author = new Author("roki", "Roki Pazi", LocalDate.of(1999, 12, 1), "Najbolji kuhar ikad", null);

        AuthorModel authorModel = assembler.toModel(author);
        assertEquals("AuthorModel(username=roki, fullName=Roki Pazi, dateOfBirth=1999-12-01, bio=Najbolji kuhar ikad, imageUrl=null)", authorModel.toString());

        author.setImageUrl(StringTestUtils.generateRandomUrl());
        assertEquals("AuthorModel(username=roki, fullName=Roki Pazi, dateOfBirth=1999-12-01, bio=Najbolji kuhar ikad, imageUrl=" + author.getImageUrl() + ")", assembler.toModel(author).toString());
    }

}
