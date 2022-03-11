package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.domain.service.AuthorService;
import net.ancronik.cookbook.backend.web.dto.author.AuthorCreateRequest;
import net.ancronik.cookbook.backend.web.dto.author.AuthorModel;
import net.ancronik.cookbook.backend.web.dto.author.AuthorUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Author}.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(AuthorController.DEFAULT_MAPPING)
@Slf4j
public class AuthorController {

    public static final String DEFAULT_MAPPING = "/api/v1/authors";

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<AuthorModel> getAuthorById(@PathVariable String id)
            throws DataDoesNotExistException, GenericDatabaseException {
        LOG.info("Fetching author with id [{}]", id);

        return ResponseEntity.ok(authorService.getAuthor(id));
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    // TODO secure this endpoint so that only user auth app can all it
    public ResponseEntity<AuthorModel> createAuthor(@RequestBody AuthorCreateRequest request)
            throws IllegalDataInRequestException, GenericDatabaseException {
        LOG.info("Creating new author [{}]", request);

        AuthorModel response = authorService.createAuthor(request);

        return ResponseEntity.created(response.getLink(IanaLinkRelations.SELF).orElseThrow().toUri()).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    // TODO secure this endpoint so that only user can update this
    public ResponseEntity<AuthorModel> updateAuthor(@PathVariable String id, @RequestBody AuthorUpdateRequest request)
            throws DataDoesNotExistException, IllegalDataInRequestException, GenericDatabaseException {
        LOG.info("Updating author [{}]", id);

        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

}
