package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import net.ancronik.cookbook.backend.web.dto.RecipePreviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static net.ancronik.cookbook.backend.hateoas.HateoasUtilSupport.createLinksForSlicedModel;

/**
 * Controller handling all operations for the {@link net.ancronik.cookbook.backend.data.model.Recipe}.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(RecipeController.DEFAULT_MAPPING)
@ExposesResourceFor(Recipe.class)
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    public static final String DEFAULT_MAPPING = "/api/v1/recipes";

    private final EntityLinks entityLinks;

    @Autowired
    public RecipeController(RecipeService recipeService, EntityLinks entityLinks) {
        this.recipeService = recipeService;
        this.entityLinks = entityLinks;
    }

    @GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipePreviewDto> getAllRecipes(Pageable pageable) {
        log.info("Fetching all recipes [{}]", pageable);
        Slice<RecipePreviewDto> data = recipeService.getAllRecipes(pageable);

        if (null == data) {
            throw new EmptyDataException("Empty page returned by service");
        }

        return SlicedModel.of(data, createLinksForSlicedModel(data,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getAllRecipes(null)).withSelfRel().getTemplate()));
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<RecipeDto> findRecipeById(@PathVariable Long id, HttpServletResponse response) {
        log.info("Fetching recipe with id [{}]", id);
        Optional<RecipeDto> data = recipeService.getRecipe(id);

        if (data.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return EntityModel.of(data.get());
    }

    @GetMapping(value = "/category/{category}", produces = MediaTypes.HAL_JSON_VALUE)
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
