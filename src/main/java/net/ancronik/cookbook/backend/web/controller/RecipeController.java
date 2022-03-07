package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import net.ancronik.cookbook.backend.hateoas.SlicedResourcesAssembler;
import net.ancronik.cookbook.backend.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    public static final String DEFAULT_MAPPING = "/api/v1/recipes";

    private final RecipeService recipeService;

    private final RecipeCommentService recipeCommentService;

    private final HateoasPageableHandlerMethodArgumentResolver hateoasPageableHandlerMethodArgumentResolver;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeCommentService recipeCommentService, HateoasPageableHandlerMethodArgumentResolver hateoasPageableHandlerMethodArgumentResolver) {
        this.recipeService = recipeService;
        this.recipeCommentService = recipeCommentService;
        this.hateoasPageableHandlerMethodArgumentResolver = hateoasPageableHandlerMethodArgumentResolver;
    }

    @GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeBasicInfoDto> getRecipes(Pageable pageable) {
        LOG.info("Fetching all recipes [{}]", pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipes(pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getRecipes(null)).withSelfRel().getTemplate());
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RecipeDto> findRecipeById(@PathVariable Long id) throws DataDoesNotExistException {
        LOG.info("Fetching recipe with id [{}]", id);

        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @GetMapping(value = "/category/{category}", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeBasicInfoDto> getRecipesForCategory(@PathVariable String category, Pageable pageable) {
        LOG.info("Fetching recipes for category [{}] and pageable [{}]", category, pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipesForCategory(category, pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getRecipesForCategory(category, null)).withSelfRel().getTemplate());
    }

    @GetMapping(value = "/{id}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<RecipeCommentDto> getCommentsForRecipe(@PathVariable Long id, Pageable pageable) throws DataDoesNotExistException {
        LOG.info("Fetching comments for recipe with id [{}] and pageable [{}]", id, pageable);

        return checkAndConvertSliceToModel(recipeCommentService.getCommentsForRecipe(id, pageable),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeController.class).getCommentsForRecipe(id, pageable)).withSelfRel().getTemplate());
    }

    @PostMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    //TODO add security to make user authorized and can't spam this endpoint
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeCreateRequest request) throws IllegalDataInRequestException {
        LOG.info("Creating new recipe {}", request);

        RecipeDto response = recipeService.createRecipe(request);

        return ResponseEntity.created(response.getLink("self").orElseThrow().toUri()).body(response);
    }

    @PostMapping(value = "/{id}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    //TODO add security to make user authorized and can't spam this endpoint
    public ResponseEntity<String> addCommentToRecipe(@PathVariable Long id, @RequestBody AddCommentRequest request) throws DataDoesNotExistException, IllegalDataInRequestException {
        LOG.info("Adding new comment to recipe [{}]", id);

        recipeCommentService.addCommentToRecipe(id, request);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    //TODO add security to make user authorized and can't spam this endpoint and it is the author of given recipe
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long id, @RequestBody RecipeUpdateRequest request) throws DataDoesNotExistException, IllegalDataInRequestException {
        LOG.info("Updating recipe with id [{}]: [{}]", id, request);

        return ResponseEntity.ok(recipeService.updateRecipe(id, request));
    }

    @DeleteMapping(value = "/{id}")
    //TODO add security to make user authorized and it is the author of given recipe
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) throws DataDoesNotExistException {
        LOG.info("Deleting recipe with id [{}]", id);

        recipeService.deleteRecipe(id);

        return ResponseEntity.ok().build();
    }


    private <T extends RepresentationModel<T>> SlicedModel<T> checkAndConvertSliceToModel(Slice<T> data, UriTemplate template) {
        if (null == data) {
            LOG.error("Null data returned by service. [{}]", template.toString());
            throw new EmptyDataException("Null data returned by service");
        }

        SlicedResourcesAssembler<T> assembler = new SlicedResourcesAssembler<>(hateoasPageableHandlerMethodArgumentResolver, null);

        return (SlicedModel<T>) assembler.toModel(data);
    }

}
