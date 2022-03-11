package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.data.model.Recipe;
import net.ancronik.cookbook.backend.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.domain.assembler.RecipeBasicInfoModelAssembler;
import net.ancronik.cookbook.backend.domain.assembler.RecipeModelAssembler;
import net.ancronik.cookbook.backend.domain.mapper.Mapper;
import net.ancronik.cookbook.backend.domain.mapper.RecipeCreateRequestToRecipeMapper;
import net.ancronik.cookbook.backend.domain.mapper.RecipeUpdateRequestRecipeUpdateMapper;
import net.ancronik.cookbook.backend.domain.mapper.UpdateMapper;
import net.ancronik.cookbook.backend.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Tag(TestTypes.UNIT)
public class RecipeServiceImplTest {

    private final ModelMapper modelMapper = new ModelMapper();

    private final RecipeRepository mockRecipeRepository = Mockito.mock(RecipeRepository.class);

    private final RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler = new RecipeModelAssembler(modelMapper);

    private final RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeBasicInfoModelAssembler =
            new RecipeBasicInfoModelAssembler(modelMapper);

    private final Mapper<RecipeCreateRequest, Recipe> createRequestRecipeMapper = new RecipeCreateRequestToRecipeMapper(modelMapper);

    private final UpdateMapper<RecipeUpdateRequest, Recipe> updateRequestRecipeUpdateMapper = new RecipeUpdateRequestRecipeUpdateMapper(modelMapper);

    private final AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);

    private final RecipeServiceImpl recipeService = new RecipeServiceImpl(mockRecipeRepository, recipeModelAssembler, recipeBasicInfoModelAssembler
            , createRequestRecipeMapper, updateRequestRecipeUpdateMapper, mockAuthenticationService);

    @Test
    public void getRecipes_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findAll(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipes(Pageable.ofSize(10)));

        verify(mockRecipeRepository).findAll(PageRequest.of(0, 10));
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipes_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipes(pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipes_RepositoryReturnsData_ReturnData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(
                new SliceImpl<>(RecipeMockData.generateRandomMockData(pageable.getPageSize()), pageable, true));
        assertEquals(10, recipeService.getRecipes(pageable).getNumberOfElements());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipe(null));

        verifyNoInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipe(19L));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @SneakyThrows
    @Test
    public void getRecipe_RepositoryReturnsEntity_ReturnModel() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(RecipeMockData.generateRandomMockData(1).get(0)));

        assertNotNull(recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipesForCategory_CategoryIsNull_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipesForCategory(null, Pageable.ofSize(10)));

        verifyNoInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipesForCategory_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findAllByCategory(any(), any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipesForCategory("desserts", Pageable.ofSize(10)));

        verify(mockRecipeRepository).findAllByCategory(any(), eq(PageRequest.of(0, 10)));
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipesForCategory_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);
        String category = "desserts";

        when(mockRecipeRepository.findAllByCategory(category, pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipesForCategory(category, pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAllByCategory(category, pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
    }

    @Test
    public void getRecipesForCategory_RepositoryReturnsData_ReturnData() {
        Pageable pageable = Pageable.ofSize(10);
        String category = "desserts";

        when(mockRecipeRepository.findAllByCategory(category, pageable)).thenReturn(new SliceImpl<>(RecipeMockData.generateRandomMockData(10),
                pageable, false));
        assertEquals(10, recipeService.getRecipesForCategory(category, pageable).getContent().size());

        verify(mockRecipeRepository).findAllByCategory(category, pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
    }
}
