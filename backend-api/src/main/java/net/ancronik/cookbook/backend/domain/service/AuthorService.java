package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
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
     */
    AuthorModel getAuthor(@NonNull String id) throws DataDoesNotExistException;

    /**
     * Method for creating new author.
     *
     * @param request request
     * @return newly created author
     */
    AuthorModel createAuthor(@NonNull AuthorCreateRequest request);

    /**
     * Method for updating author.
     *
     * @param id      id
     * @param request request
     * @return updated author
     * @throws DataDoesNotExistException     in case that author does not exist
     */
    AuthorModel updateAuthor(@NonNull String id, @NonNull AuthorUpdateRequest request) throws DataDoesNotExistException;
}
