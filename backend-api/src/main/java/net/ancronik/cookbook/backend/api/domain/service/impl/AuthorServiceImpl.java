package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.data.model.Author;
import net.ancronik.cookbook.backend.api.data.repository.AuthorRepository;
import net.ancronik.cookbook.backend.api.domain.service.AuthorService;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Service
@Slf4j
@Validated
public class AuthorServiceImpl implements AuthorService {

    private final RepresentationModelAssemblerSupport<Author, AuthorModel> authorModelAssembler;

    private final AuthorRepository authorRepository;


    @Override
    public AuthorModel getAuthor(@NonNull @NotBlank @CodePointLength(min = 2, max = 12) String id) throws DataDoesNotExistException, ConstraintViolationException {
        LOG.debug("Searching author with id [{}]", id);

        try {
            Author author = authorRepository.findById(id)
                .orElseThrow(() -> new DataDoesNotExistException("Author with given username does not exists: " + id));

            return authorModelAssembler.toModel(author);
        } catch (DataDoesNotExistException e) {
            LOG.error("Author with id [{}] does not exists", id);
            throw e;
        }
    }
}
