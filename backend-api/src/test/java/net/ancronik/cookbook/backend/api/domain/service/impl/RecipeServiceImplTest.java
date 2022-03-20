package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.api.application.exceptions.UnauthorizedActionException;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.api.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.api.domain.assembler.RecipeBasicInfoModelAssembler;
import net.ancronik.cookbook.backend.api.domain.assembler.RecipeModelAssembler;
import net.ancronik.cookbook.backend.api.domain.mapper.Mapper;
import net.ancronik.cookbook.backend.api.domain.mapper.RecipeCreateRequestToRecipeMapper;
import net.ancronik.cookbook.backend.api.domain.mapper.RecipeUpdateRequestRecipeUpdateMapper;
import net.ancronik.cookbook.backend.api.domain.mapper.UpdateMapper;
import net.ancronik.cookbook.backend.api.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.api.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCreateRequest;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeUpdateRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipes_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipes(pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipes_RepositoryReturnsData_ReturnData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(
                new SliceImpl<>(RecipeMockData.generateRandomMockData(pageable.getPageSize()), pageable, true));
        assertEquals(10, recipeService.getRecipes(pageable).getNumberOfElements());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipe(null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipe(19L));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @SneakyThrows
    @Test
    public void getRecipe_RepositoryReturnsEntity_ReturnModel() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(RecipeMockData.generateRandomMockData(1).get(0)));

        assertNotNull(recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipesForCategory_CategoryIsNull_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipesForCategory(null, Pageable.ofSize(10)));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipesForCategory_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findAllByCategory(any(), any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipesForCategory("desserts", Pageable.ofSize(10)));

        verify(mockRecipeRepository).findAllByCategory(any(), eq(PageRequest.of(0, 10)));
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void getRecipesForCategory_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);
        String category = "desserts";

        when(mockRecipeRepository.findAllByCategory(category, pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipesForCategory(category, pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAllByCategory(category, pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
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
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void createRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.save(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.createRecipe(DtoMockData.generateRandomMockDataForRecipeCreateRequest()));

        verify(mockRecipeRepository).save(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void createRecipe_SaveSuccessful_ReturnModel() {
        when(mockRecipeRepository.save(any())).thenReturn(RecipeMockData.generateRandomMockData(1).get(0));

        assertNotNull(recipeService.createRecipe(DtoMockData.generateRandomMockDataForRecipeCreateRequest()));

        verify(mockRecipeRepository).save(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.deleteRecipe(null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.deleteRecipe(19L));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.deleteRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_AuthenticationServiceThrowsException_PropagateException() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenThrow(new EmptyDataException("test"));

        assertThrows(EmptyDataException.class, () -> recipeService.deleteRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_UserIsNotTheAuthorOfRecipe_ThrowUnauthorizedActionException() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenReturn(false);

        assertThrows(UnauthorizedActionException.class, () -> recipeService.deleteRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verify(mockAuthenticationService).getAuthenticatedUsername();
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @Test
    public void deleteRecipe_DeleteSuccessful() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenReturn(true);

        assertDoesNotThrow(() -> recipeService.deleteRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verify(mockRecipeRepository).deleteById(11L);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @Test
    public void updateRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(null, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));
        assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(1L, null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void updateRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.updateRecipe(19L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void updateRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.updateRecipe(11L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    public void updateRecipe_AuthenticationServiceThrowsException_PropagateException() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenThrow(new EmptyDataException("test"));

        assertThrows(EmptyDataException.class, () -> recipeService.updateRecipe(11L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(11L);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @Test
    public void updateRecipe_UserIsNotTheAuthorOfRecipe_ThrowUnauthorizedActionException() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenReturn(false);

        assertThrows(UnauthorizedActionException.class, () -> recipeService.updateRecipe(11L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(11L);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verify(mockAuthenticationService).getAuthenticatedUsername();
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }

    @SneakyThrows
    @Test
    public void updateRecipe_UpdateSuccessful_ReturnModel() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        RecipeUpdateRequest updateRequest = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();

        updateRequestRecipeUpdateMapper.update(updateRequest, recipe);

        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(recipe));
        when(mockRecipeRepository.save(recipe)).thenReturn(recipe);
        when(mockAuthenticationService.isGivenUserTheRequester(recipe.getAuthorId())).thenReturn(true);

        assertNotNull(recipeService.updateRecipe(11L, updateRequest));

        verify(mockRecipeRepository).findById(11L);
        verify(mockRecipeRepository).save(recipe);
        verify(mockAuthenticationService).isGivenUserTheRequester(recipe.getAuthorId());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoMoreInteractions(mockAuthenticationService);
    }
}
