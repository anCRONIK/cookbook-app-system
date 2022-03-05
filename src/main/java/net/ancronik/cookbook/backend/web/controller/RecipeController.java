package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static net.ancronik.cookbook.backend.application.HateoasUtilSupport.createLinksForSlicedModel;

/**
 * Controller handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(RecipeController.DEFAULT_MAPPING)
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    public static final String DEFAULT_MAPPING = "/api/v1/recipes";

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public SlicedModel<RecipePreviewDto> getAllRecipes(Pageable pageable) {
        log.info("Fetching all recipes [{}]", pageable);
        Slice<RecipePreviewDto> data = recipeService.getAllRecipes(pageable);

        if (null == data) {
            throw new EmptyDataException("Empty page returned by service");
        }

        return SlicedModel.of(data, createLinksForSlicedModel(data,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipes(null)).withSelfRel().getTemplate()));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeDto> findRecipeById(@PathVariable Long id) {
        log.info("Fetching recipe with id [{}]", id);
        Optional<RecipeDto> data = recipeService.getRecipe(id);

        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(data.get());
    }

    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SlicedModel<RecipePreviewDto> getAllRecipesForCategory(@PathVariable String category, Pageable pageable) {
        log.info("Fetching recipes for category [{}] and pageable [{}]", category, pageable);
        Slice<RecipePreviewDto> data = recipeService.getAllRecipesForCategory(category, pageable);

        if (null == data) {
            throw new EmptyDataException("Empty page returned by service");
        }

        return SlicedModel.of(data, createLinksForSlicedModel(data,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipesForCategory(category, null)).withSelfRel().getTemplate()));
    }

}
