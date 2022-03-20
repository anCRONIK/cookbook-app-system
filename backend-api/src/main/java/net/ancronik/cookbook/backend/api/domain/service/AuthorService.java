package net.ancronik.cookbook.backend.api.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorUpdateRequest;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Service handling all operations for the {@link Author}.
 *
 * @author Nikola Presecki
 */
public interface AuthorService {

    /**
     * Method for finding author by username
     *
     * @param id username, can't be null and must have at least 2 characters and maximum 12
     * @return author
     * @throws DataDoesNotExistException    in case that author does not exist
     * @throws ConstraintViolationException if given id is not in valid range
     */
    AuthorModel getAuthor(@NonNull @NotBlank @CodePointLength(min = 2, max = 12) String id) throws DataDoesNotExistException, ConstraintViolationException;

    /**
     * Method for creating new author.
     * <p>
     * Username in the request will not be checked for uniqueness because user handling is not in the scope of this application.
     * Authors are created based on registered users, and it is internal scope.
     *
     * @param request request, can't be null and all values will be validated
     * @return newly created author
     * @throws ConstraintViolationException if request fails validation check
     */
    @Transactional
    AuthorModel createAuthor(@NonNull @Valid AuthorCreateRequest request) throws ConstraintViolationException;

    /**
     * Method for updating author.
     *
     * @param id      id, can't be null and must have at least 2 characters and maximum 12
     * @param request request, can't be null and all values will be validated
     * @return updated author
     * @throws DataDoesNotExistException    in case that author does not exist
     * @throws ConstraintViolationException if id or request are not valid
     */
    @Transactional
    AuthorModel updateAuthor(@NonNull @NotBlank @CodePointLength(min = 2, max = 12) String id, @NonNull @Valid AuthorUpdateRequest request)
            throws DataDoesNotExistException, ConstraintViolationException;
}
