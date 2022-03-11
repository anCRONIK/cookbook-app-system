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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AuthorService}.
 *
 * @author Nikola Presecki
 */
@Service
@Slf4j
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
    public AuthorModel getAuthor(@NonNull String id) throws DataDoesNotExistException {
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

    @Override
    public AuthorModel createAuthor(@NonNull AuthorCreateRequest request) {
        LOG.debug("Saving new author [{}]", request);

        Author author = authorRepository.save(authorCreateRequestToAuthorMapper.map(request));

        return authorModelAssembler.toModel(author);
    }

    @Override
    public AuthorModel updateAuthor(@NonNull String id, @NonNull AuthorUpdateRequest request) throws DataDoesNotExistException {
        LOG.debug("Updating author [{}] with data [{}]", id, request);

        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new DataDoesNotExistException("Author with given username does not exists: " + id));

            authorUpdateRequestToAuthorMapper.update(request, author);

            return authorModelAssembler.toModel(authorRepository.save(author));
        } catch (DataDoesNotExistException e) {
            LOG.error("Author with id [{}] does not exists", id);
            throw e;
        }
    }
}
