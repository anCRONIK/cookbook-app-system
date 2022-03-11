package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.data.model.AuthorMockData;
import net.ancronik.cookbook.backend.data.repository.AuthorRepository;
import net.ancronik.cookbook.backend.domain.assembler.AuthorModelAssembler;
import net.ancronik.cookbook.backend.domain.mapper.AuthorCreateRequestToAuthorMapper;
import net.ancronik.cookbook.backend.domain.mapper.AuthorUpdateRequestAuthorUpdateMapper;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag(TestTypes.UNIT)
public class AuthorServiceImplTest {

    private final ModelMapper modelMapper = new ModelMapper();

    private final AuthorModelAssembler authorModelAssembler = new AuthorModelAssembler(modelMapper);

    private final AuthorRepository mockAuthorRepository = Mockito.mock(AuthorRepository.class);

    private final AuthorCreateRequestToAuthorMapper authorCreateRequestToAuthorMapper = new AuthorCreateRequestToAuthorMapper(modelMapper);

    private final AuthorUpdateRequestAuthorUpdateMapper authorUpdateRequestAuthorUpdateMapper = new AuthorUpdateRequestAuthorUpdateMapper(modelMapper);

    private final AuthorServiceImpl authorService = new AuthorServiceImpl(authorModelAssembler, mockAuthorRepository, authorCreateRequestToAuthorMapper, authorUpdateRequestAuthorUpdateMapper);


    @Test
    public void getAuthor_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthor(null));

        verifyNoInteractions(mockAuthorRepository);
    }

    @Test
    public void getAuthor_RepositoryThrowsException_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> authorService.getAuthor(id));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @Test
    public void getAuthor_AuthorDoesNotExists_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> authorService.getAuthor(id));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @SneakyThrows
    @Test
    public void getAuthor_AuthorExists_ReturnModel() {
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
    public void createAuthor_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> authorService.createAuthor(null));

        verifyNoInteractions(mockAuthorRepository);
    }

    @Test
    public void createAuthor_RepositoryThrowsException_ThrowException() {
        String username = "testUser";
        AuthorCreateRequest authorCreateRequest = new AuthorCreateRequest(username);

        when(mockAuthorRepository.save(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> authorService.createAuthor(authorCreateRequest));

        verify(mockAuthorRepository).save(any());
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @SneakyThrows
    @Test
    public void createAuthor_AuthorSuccessfullyCreated_ReturnModel() {
        Author author = AuthorMockData.generateRandomMockData(1).get(0);
        String username = "testUser";
        AuthorCreateRequest authorCreateRequest = new AuthorCreateRequest(username);

        when(mockAuthorRepository.save(any())).thenReturn(author);

        AuthorModel dto = authorService.createAuthor(authorCreateRequest);

        assertNotNull(dto);
        assertNotNull(dto.getUsername());

        verify(mockAuthorRepository).save(any());
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @Test
    public void updateAuthor_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> authorService.updateAuthor(null, new AuthorUpdateRequest()));
        assertThrows(IllegalArgumentException.class, () -> authorService.updateAuthor("id", null));

        verifyNoInteractions(mockAuthorRepository);
    }

    @Test
    public void updateAuthor_RepositoryThrowsException_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> authorService.updateAuthor(id, new AuthorUpdateRequest()));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @Test
    public void updateAuthor_AuthorDoesNotExists_ThrowException() {
        String id = "testUser";

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> authorService.updateAuthor(id, new AuthorUpdateRequest()));

        verify(mockAuthorRepository).findById(id);
        verifyNoMoreInteractions(mockAuthorRepository);
    }

    @SneakyThrows
    @Test
    public void updateAuthor_UpdateSuccessful_ReturnModel() {
        Author author = AuthorMockData.generateRandomMockData(1).get(0);
        String id = author.getUsername();
        AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest();
        authorUpdateRequest.setFullName("Ivo Ivic");
        authorUpdateRequest.setBio("Awesome Chef!");

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.of(author));
        authorUpdateRequestAuthorUpdateMapper.update(authorUpdateRequest, author);
        when(mockAuthorRepository.save(any(Author.class))).thenReturn(author);

        AuthorModel dto = authorService.updateAuthor(id, authorUpdateRequest);
        assertNotNull(dto);
        assertNotNull(dto.getUsername());

        verify(mockAuthorRepository).findById(id);
        verify(mockAuthorRepository).save(author);
        verifyNoMoreInteractions(mockAuthorRepository);
    }
}
