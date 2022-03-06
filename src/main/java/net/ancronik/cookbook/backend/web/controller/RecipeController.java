package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import net.ancronik.cookbook.backend.web.dto.RecipeBasicInfoDto;
import net.ancronik.cookbook.backend.web.dto.RecipeCommentDto;
import net.ancronik.cookbook.backend.web.dto.RecipeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeBasicInfoDto> getRecipes(Pageable pageable) {
        log.info("Fetching all recipes [{}]", pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipes(pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getRecipes(null)).withSelfRel().getTemplate());
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RecipeDto> findRecipeById(@PathVariable Long id) {
        log.info("Fetching recipe with id [{}]", id);
        Optional<RecipeDto> data = recipeService.getRecipe(id);

        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(data.get());
    }

    @GetMapping(value = "/category/{category}", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeBasicInfoDto> getRecipesForCategory(@PathVariable String category, Pageable pageable) {
        log.info("Fetching recipes for category [{}] and pageable [{}]", category, pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipesForCategory(category, pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getRecipesForCategory(category, null)).withSelfRel().getTemplate());
    }

    @GetMapping(value = "/{id}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeCommentDto> getCommentsForRecipe(@PathVariable Long id, Pageable pageable) {
        log.info("Fetching comments for recipe with id [{}] and pageable [{}]", id, pageable);

        return checkAndConvertSliceToModel(recipeService.getCommentsForRecipe(id, pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getCommentsForRecipe(id, pageable)).withSelfRel().getTemplate());
    }


    private <T extends RepresentationModel<T>> SlicedModel<T> checkAndConvertSliceToModel(Slice<T> data, UriTemplate template) {
        if (null == data) {
            log.error("Null data returned by service. [{}]", template.toString());
            throw new EmptyDataException("Null data returned by service");
        }

        return SlicedModel.of(data, createLinksForSlicedModel(data, template));
    }

}
