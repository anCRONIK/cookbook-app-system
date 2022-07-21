package net.ancronik.cookbook.backend.api.domain.service;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import org.hibernate.validator.constraints.CodePointLength;

import javax.validation.ConstraintViolationException;
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
}
