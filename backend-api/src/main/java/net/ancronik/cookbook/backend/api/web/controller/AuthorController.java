package net.ancronik.cookbook.backend.api.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.domain.service.AuthorService;
import net.ancronik.cookbook.backend.api.web.dto.author.AuthorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthorModel> getAuthorById(@PathVariable String id) throws DataDoesNotExistException {
        LOG.debug("Fetching author with id [{}]", id);

        return ResponseEntity.ok(authorService.getAuthor(id));
    }
}
