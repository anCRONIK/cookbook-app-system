package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.data.model.Author;
import net.ancronik.cookbook.backend.data.repository.AuthorRepository;
import net.ancronik.cookbook.backend.domain.mapper.Mapper;
import net.ancronik.cookbook.backend.domain.mapper.UpdateMapper;
import net.ancronik.cookbook.backend.domain.service.AuthorService;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Implementation of {@link AuthorService}.
 *
 * @author Nikola Presecki
 */
@Service
@Slf4j
@Validated
public class AuthorServiceImpl implements AuthorService {

    private final RepresentationModelAssemblerSupport<Author, AuthorModel> authorModelAssembler;

    private final AuthorRepository authorRepository;

    private final Mapper<AuthorCreateRequest, Author> authorCreateRequestToAuthorMapper;

    private final UpdateMapper<AuthorUpdateRequest, Author> authorUpdateRequestToAuthorMapper;

    @Autowired
    public AuthorServiceImpl(RepresentationModelAssemblerSupport<Author, AuthorModel> authorModelAssembler, AuthorRepository authorRepository,
                             Mapper<AuthorCreateRequest, Author> authorCreateRequestToAuthorMapper,
                             UpdateMapper<AuthorUpdateRequest, Author> authorUpdateRequestToAuthorMapper) {
        this.authorModelAssembler = authorModelAssembler;
        this.authorRepository = authorRepository;
        this.authorCreateRequestToAuthorMapper = authorCreateRequestToAuthorMapper;
        this.authorUpdateRequestToAuthorMapper = authorUpdateRequestToAuthorMapper;
    }

    @Override
    public AuthorModel getAuthor(@NonNull @NotBlank  @CodePointLength(min = 2, max = 12) String id) throws DataDoesNotExistException, ConstraintViolationException {
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

    @Transactional
    @Override
    public AuthorModel createAuthor(@NonNull @Valid AuthorCreateRequest request) throws ConstraintViolationException {
        LOG.debug("Saving new author [{}]", request);

        Author author = authorRepository.save(authorCreateRequestToAuthorMapper.map(request));

        return authorModelAssembler.toModel(author);
    }

    @Transactional
    @Override
    public AuthorModel updateAuthor(@NonNull @NotBlank @CodePointLength(min = 2, max = 12) String id, @NonNull @Valid AuthorUpdateRequest request)
            throws DataDoesNotExistException, ConstraintViolationException {
        LOG.debug("Updating author [{}] with data [{}]", id, request);

        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new DataDoesNotExistException("Author with given username does not exists: " + id));

            authorUpdateRequestToAuthorMapper.update(request, author);

            return authorModelAssembler.toModel(authorRepository.save(author)); //TODO will EM handle this and we don't need to save because transactional, not sure for cassandra db?
        } catch (DataDoesNotExistException e) {
            LOG.error("Author with id [{}] does not exists", id);
            throw e;
        }
    }
}
