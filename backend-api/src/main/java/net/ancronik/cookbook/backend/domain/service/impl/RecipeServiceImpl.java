package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.UnauthorizedException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.domain.mapper.Mapper;
import net.ancronik.cookbook.backend.domain.mapper.UpdateMapper;
import net.ancronik.cookbook.backend.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.validation.annotation.PageableConstraint;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link RecipeService}
 *
 * @author Nikola Presecki
 */
@Service
@Slf4j
@Validated
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler;

    private final RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeBasicInfoModelAssembler;

    private final Mapper<RecipeCreateRequest, Recipe> createRequestRecipeMapper;

    private final UpdateMapper<RecipeUpdateRequest, Recipe> updateRequestRecipeUpdateMapper;

    private final AuthenticationService authenticationService;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler,
                             RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeBasicInfoModelAssembler,
                             Mapper<RecipeCreateRequest, Recipe> createRequestRecipeMapper,
                             UpdateMapper<RecipeUpdateRequest, Recipe> updateRequestRecipeUpdateMapper,
                             AuthenticationService authenticationService) {
        this.recipeRepository = recipeRepository;
        this.recipeModelAssembler = recipeModelAssembler;
        this.recipeBasicInfoModelAssembler = recipeBasicInfoModelAssembler;
        this.createRequestRecipeMapper = createRequestRecipeMapper;
        this.updateRequestRecipeUpdateMapper = updateRequestRecipeUpdateMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public Slice<RecipeBasicInfoModel> getRecipes(@NonNull @PageableConstraint Pageable pageable) throws ConstraintViolationException {
        LOG.info("Searching recipes with pageable [{}]", pageable);
        Slice<Recipe> data = recipeRepository.findAll(pageable);

        List<RecipeBasicInfoModel> dtoList = data.getContent().stream().map(recipeBasicInfoModelAssembler::toModel).collect(Collectors.toList());

        return new SliceImpl<>(dtoList, data.getPageable(), data.hasNext());
    }

    @Override
    public RecipeModel getRecipe(@NonNull @NotNull @Range(min = 1) Long id) throws DataDoesNotExistException, ConstraintViolationException {
        LOG.info("Searching recipe with id [{}]", id);

        try {
            return recipeModelAssembler.toModel(
                    recipeRepository.findById(id).orElseThrow(() -> new DataDoesNotExistException("Recipe does not exits: " + id)));
        } catch (DataDoesNotExistException e) {
            LOG.error("Recipe with id [{}] does not exists", id);
            throw e;
        }
    }

    @Override
    public Slice<RecipeBasicInfoModel> getRecipesForCategory(@NonNull @NotBlank @Size(min = 1, max = 50) String category,
                                                             @NonNull @PageableConstraint Pageable pageable) throws ConstraintViolationException {
        LOG.info("Searching recipes in category [{}] with pageable [{}]", category, pageable);

        Slice<Recipe> data = recipeRepository.findAllByCategory(category, pageable);

        List<RecipeBasicInfoModel> dtoList = data.getContent().stream().map(recipeBasicInfoModelAssembler::toModel).collect(Collectors.toList());

        return new SliceImpl<>(dtoList, data.getPageable(), data.hasNext());
    }

    @Override
    @Transactional
    public RecipeModel createRecipe(@NonNull @NotNull @Valid RecipeCreateRequest request) throws ConstraintViolationException {
        LOG.info("Creating new recipe [{}]", request);

        Recipe recipe = recipeRepository.save(createRequestRecipeMapper.map(request));

        return recipeModelAssembler.toModel(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(@NonNull @NotNull @Range(min = 1) Long id) throws DataDoesNotExistException, ConstraintViolationException,
            UnauthorizedException {
        LOG.info("Deleting recipe with id: [{}]", id);

        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new DataDoesNotExistException("Recipe does not exits: " + id));
            if (authenticationService.isGivenUserTheRequester(recipe.getAuthorId())) {
                recipeRepository.deleteById(id);
            } else {
                LOG.info("User [{}] is not authorized to delete recipe [{}]. Incident!", authenticationService.getAuthenticatedUsername(), id);
                throw new UnauthorizedException("User is not authorized to delete recipe: " + id);
            }
        } catch (DataDoesNotExistException e) {
            LOG.error("Recipe with id [{}] does not exists", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public RecipeModel updateRecipe(@NonNull @NotNull @Range(min = 1) Long id, @NonNull @NotNull @Valid RecipeUpdateRequest request) throws DataDoesNotExistException,
            ConstraintViolationException, UnauthorizedException {
        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new DataDoesNotExistException("Recipe does not exits: " + id));
            if (authenticationService.getAuthenticatedUsername().equalsIgnoreCase(recipe.getAuthorId())) {
                updateRequestRecipeUpdateMapper.update(request, recipe);
                return recipeModelAssembler.toModel(recipeRepository.save(recipe));
            } else {
                LOG.info("User [{}] is not authorized to update recipe [{}]. Incident!", authenticationService.getAuthenticatedUsername(), id);
                throw new UnauthorizedException("User is not authorized to update recipe: " + id);
            }
        } catch (DataDoesNotExistException e) {
            LOG.error("Recipe with id [{}] does not exists", id);
            throw e;
        }
    }

}
