package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.domain.service.RecipeCommentService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.hateoas.SlicedModel;
import net.ancronik.cookbook.backend.hateoas.SlicedResourcesAssembler;
import net.ancronik.cookbook.backend.web.dto.recipe.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.*;
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
@Slf4j
public class RecipeController {

    public static final String DEFAULT_MAPPING = "/api/v1/recipes";

    private final RecipeService recipeService;

    private final RecipeCommentService recipeCommentService;

    private final CodeQueryService codeQueryService;

    private final HateoasPageableHandlerMethodArgumentResolver hateoasPageableHandlerMethodArgumentResolver;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeCommentService recipeCommentService, CodeQueryService codeQueryService, HateoasPageableHandlerMethodArgumentResolver hateoasPageableHandlerMethodArgumentResolver) {
        this.recipeService = recipeService;
        this.recipeCommentService = recipeCommentService;
        this.codeQueryService = codeQueryService;
        this.hateoasPageableHandlerMethodArgumentResolver = hateoasPageableHandlerMethodArgumentResolver;
    }

    @GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<?> getRecipes(Pageable pageable) {
        LOG.info("Fetching all recipes [{}]", pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipes(pageable));
    }


    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) throws DataDoesNotExistException {
        LOG.info("Fetching recipe with id [{}]", id);

        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @GetMapping(value = "/categories/{category}", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<?> getRecipesForCategory(@PathVariable String category, Pageable pageable) {
        LOG.info("Fetching recipes for category [{}] and pageable [{}]", category, pageable);

        return checkAndConvertSliceToModel(recipeService.getRecipesForCategory(category, pageable));
    }

    @GetMapping(value = "/{id}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public SlicedModel<?> getCommentsForRecipe(@PathVariable Long id, Pageable pageable) throws DataDoesNotExistException {
        LOG.info("Fetching comments for recipe with id [{}] and pageable [{}]", id, pageable);

        return checkAndConvertSliceToModel(recipeCommentService.getCommentsForRecipe(id, pageable));
    }

    @PostMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    //TODO add security to make user authorized and can't spam this endpoint
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeCreateRequest request) throws IllegalDataInRequestException {
        LOG.info("Creating new recipe {}", request);

        RecipeDto response = recipeService.createRecipe(request);

        return ResponseEntity.created(response.getLink(IanaLinkRelations.SELF).orElseThrow().toUri()).body(response);
    }

    @PostMapping(value = "/{id}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    //TODO add security to make user authorized and can't spam this endpoint
    public ResponseEntity<String> addCommentToRecipe(@PathVariable Long id, @RequestBody AddRecipeCommentRequest request) throws DataDoesNotExistException, IllegalDataInRequestException {
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

    @GetMapping(value = "/categories", produces = MediaTypes.HAL_JSON_VALUE)
    @Cacheable("recipe_categories")
    public CollectionModel<RecipeCategoryDto> getRecipeCategories() throws GenericDatabaseException {
        LOG.info("Fetching all recipe categories");

        return CollectionModel.of(codeQueryService.getRecipeCategories());
    }


    private <T extends RepresentationModel<T>> SlicedModel<EntityModel<T>> checkAndConvertSliceToModel(Slice<T> data) {
        if (null == data) {
            LOG.error("Null data returned by service.");
            throw new EmptyDataException("Null data returned by service");
        }

        SlicedResourcesAssembler<T> assembler = new SlicedResourcesAssembler<>(hateoasPageableHandlerMethodArgumentResolver, null);

        return assembler.toModel(data);
    }

}
