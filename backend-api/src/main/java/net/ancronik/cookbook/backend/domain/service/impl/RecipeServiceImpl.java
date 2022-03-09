package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.NonNull;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.application.exceptions.IllegalDataInRequestException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.repository.AuthorRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.domain.mapper.Mapper;
import net.ancronik.cookbook.backend.domain.mapper.UpdateMapper;
import net.ancronik.cookbook.backend.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.domain.service.RecipeService;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link RecipeService}
 *
 * @author Nikola Presecki
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final AuthorRepository authorRepository;

    private final RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler;

    private final RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeBasicInfoModelAssembler;

    private final Mapper<RecipeCreateRequest, Recipe> createRequestRecipeMapper;

    private final UpdateMapper<RecipeUpdateRequest, Recipe> updateRequestRecipeUpdateMapper;

    private final AuthenticationService authenticationService;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, AuthorRepository authorRepository,
                             RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler,
                             RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeBasicInfoModelAssembler,
                             Mapper<RecipeCreateRequest, Recipe> createRequestRecipeMapper,
                             UpdateMapper<RecipeUpdateRequest, Recipe> updateRequestRecipeUpdateMapper,
                             AuthenticationService authenticationService) {
        this.recipeRepository = recipeRepository;
        this.authorRepository = authorRepository;
        this.recipeModelAssembler = recipeModelAssembler;
        this.recipeBasicInfoModelAssembler = recipeBasicInfoModelAssembler;
        this.createRequestRecipeMapper = createRequestRecipeMapper;
        this.updateRequestRecipeUpdateMapper = updateRequestRecipeUpdateMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public Slice<RecipeBasicInfoModel> getRecipes(Pageable pageable) {
        return null;
    }

    @Override
    public RecipeModel getRecipe(@NonNull Long id) throws DataDoesNotExistException {
        return null;
    }

    @Override
    public Slice<RecipeBasicInfoModel> getRecipesForCategory(@NonNull String category, Pageable pageable) {
        return null;
    }

    @Override
    public RecipeModel createRecipe(@NonNull RecipeCreateRequest request) throws IllegalDataInRequestException {
        return null;
    }

    @Override
    public void deleteRecipe(@NonNull Long id) throws DataDoesNotExistException {

    }

    @Override
    public RecipeModel updateRecipe(@NonNull Long id, @NonNull RecipeUpdateRequest request) throws DataDoesNotExistException, IllegalDataInRequestException {
        return null;
    }
}
