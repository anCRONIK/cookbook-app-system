package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.StringTestUtils;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.data.model.AuthorMockData;
import net.ancronik.cookbook.backend.api.data.repository.AuthorRepository;
import net.ancronik.cookbook.backend.api.domain.assembler.AuthorModelAssembler;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag(TestTypes.UNIT)
class AuthorServiceImplTest {

    private ModelMapper modelMapper = new ModelMapper();

    private AuthorModelAssembler authorModelAssembler = new AuthorModelAssembler(modelMapper);

    private AuthorRepository mockAuthorRepository = Mockito.mock(AuthorRepository.class);


    @InjectMocks
    private AuthorServiceImpl authorService;


    @Test
    void getAuthor_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthor(null));

        verifyNoInteractions(mockAuthorRepository);
    }

    @Test
    void getAuthor_RepositoryThrowsException_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> authorService.getAuthor(id));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @Test
    void getAuthor_AuthorDoesNotExists_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> authorService.getAuthor(id));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @SneakyThrows
    @Test
    void getAuthor_AuthorExists_ReturnModel() {
        Author author = AuthorMockData.generateRandomMockData(1).get(0);

        String id = author.getUsername();

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorModel dto = authorService.getAuthor(id);

        assertNotNull(dto);
        assertNotNull(dto.getUsername());

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @Test
    void getAuthor_InvalidId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(null));
        assertEquals("getAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(""));
        assertTrue(t.getMessage().startsWith("getAuthor.id:"));

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor("   \t   \r"));
        assertEquals("getAuthor.id: must not be blank", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor("a"));
        assertEquals("getAuthor.id: length must be between 2 and 12", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> authorService.getAuthor(StringTestUtils.getRandomStringInLowerCase(13)));
        assertEquals("getAuthor.id: length must be between 2 and 12", t.getMessage());
    }
}
