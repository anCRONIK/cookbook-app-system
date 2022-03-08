package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorDto;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
public interface AuthorService {

    /**
     * Method for finding author by username
     *
     * @param id username
     * @return author
     * @throws DataDoesNotExistException in case that author does not exist
     * @throws GenericDatabaseException  in case of db exception
     */
    AuthorDto getAuthor(@NonNull String id) throws DataDoesNotExistException, GenericDatabaseException;

    /**
     * Method for creating new author.
     *
     * @param request request
     * @return newly created author
     * @throws IllegalDataInRequestException in case that request data is invalid
     * @throws GenericDatabaseException      in case of db exception
     */
    AuthorDto createAuthor(@NonNull AuthorCreateRequest request) throws IllegalDataInRequestException, GenericDatabaseException;

    /**
     * Method for updating author.
     *
     * @param id      id
     * @param request request
     * @return updated author
     * @throws DataDoesNotExistException     in case that author does not exist
     * @throws IllegalDataInRequestException in case that request data is invalid
     * @throws GenericDatabaseException      in case of db exception
     */
    AuthorDto updateAuthor(String id, @NonNull AuthorUpdateRequest request) throws IllegalDataInRequestException, DataDoesNotExistException, GenericDatabaseException;
}
