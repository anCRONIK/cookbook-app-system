package net.ancronik.cookbook.backend.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Service handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
public interface AuthorService {

    /**
     * Method for finding author by username
     *
     * @param id username, can't be null and must have at least 2 characters and maximum 12
     * @return author
     * @throws DataDoesNotExistException in case that author does not exist
     */
    AuthorModel getAuthor(@NonNull @Size(min = 2, max = 12) String id) throws DataDoesNotExistException;

    /**
     * Method for creating new author.
     *
     * @param request request, can't be null and all values will be validated
     * @return newly created author
     */
    AuthorModel createAuthor(@NonNull @Valid AuthorCreateRequest request);

    /**
     * Method for updating author.
     *
     * @param id      id, can't be null and must have at least 2 characters and maximum 12
     * @param request request, can't be null and all values will be validated
     * @return updated author
     * @throws DataDoesNotExistException     in case that author does not exist
     */
    AuthorModel updateAuthor(@NonNull @Size(min = 2, max = 12) String id, @NonNull @Valid AuthorUpdateRequest request)
            throws DataDoesNotExistException;
}
