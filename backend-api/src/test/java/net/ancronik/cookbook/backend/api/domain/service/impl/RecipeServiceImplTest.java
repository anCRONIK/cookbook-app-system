package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.application.exceptions.DataDoesNotExistException;
import net.ancronik.cookbook.backend.api.application.exceptions.EmptyDataException;
import net.ancronik.cookbook.backend.api.application.exceptions.UnauthorizedActionException;
import net.ancronik.cookbook.backend.api.data.model.Recipe;
import net.ancronik.cookbook.backend.api.data.model.RecipeMockData;
import net.ancronik.cookbook.backend.api.data.repository.RecipeRepository;
import net.ancronik.cookbook.backend.api.domain.mapper.RecipeMapper;
import net.ancronik.cookbook.backend.api.domain.service.AuthenticationService;
import net.ancronik.cookbook.backend.api.web.dto.DtoMockData;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeBasicInfoModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeUpdateRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag(TestTypes.UNIT)
class RecipeServiceImplTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RecipeMapper mockRecipeMapper;
    @Mock
    private RecipeRepository mockRecipeRepository;
    @Mock
    private RepresentationModelAssemblerSupport<Recipe, RecipeModel> recipeModelAssembler;
    @Mock
    private RepresentationModelAssemblerSupport<Recipe, RecipeBasicInfoModel> recipeRecipeBasicInfoModelRepresentationModelAssemblerSupport;
    @Mock
    private AuthenticationService mockAuthenticationService;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void getRecipes_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findAll(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipes(Pageable.ofSize(10)));

        verify(mockRecipeRepository).findAll(PageRequest.of(0, 10));
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipes_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipes(pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipes_RepositoryReturnsData_ReturnData() {
        Pageable pageable = Pageable.ofSize(10);

        when(mockRecipeRepository.findAll(pageable)).thenReturn(
            new SliceImpl<>(RecipeMockData.generateRandomMockData(pageable.getPageSize()), pageable, true));
        assertEquals(10, recipeService.getRecipes(pageable).getNumberOfElements());

        verify(mockRecipeRepository).findAll(pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipe(null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipe(19L));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @SneakyThrows
    @Test
    void getRecipe_RepositoryReturnsEntity_ReturnModel() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.of(RecipeMockData.generateRandomMockData(1).get(0)));

        assertNotNull(recipeService.getRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipesForCategory_CategoryIsNull_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.getRecipesForCategory(null, Pageable.ofSize(10)));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipesForCategory_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findAllByCategory(any(), any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.getRecipesForCategory("desserts", Pageable.ofSize(10)));

        verify(mockRecipeRepository).findAllByCategory(any(), eq(PageRequest.of(0, 10)));
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipesForCategory_RepositoryReturnsEmptyData_ReturnEmptyData() {
        Pageable pageable = Pageable.ofSize(10);
        String category = "desserts";

        when(mockRecipeRepository.findAllByCategory(category, pageable)).thenReturn(new SliceImpl<>(new ArrayList<>(), pageable, false));
        assertTrue(recipeService.getRecipesForCategory(category, pageable).getContent().isEmpty());

        verify(mockRecipeRepository).findAllByCategory(category, pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void getRecipesForCategory_RepositoryReturnsData_ReturnData() {
        Pageable pageable = Pageable.ofSize(10);
        String category = "desserts";

        when(mockRecipeRepository.findAllByCategory(category, pageable)).thenReturn(new SliceImpl<>(RecipeMockData.generateRandomMockData(10),
                                                                                                    pageable, false
        ));
        assertEquals(10, recipeService.getRecipesForCategory(category, pageable).getContent().size());

        verify(mockRecipeRepository).findAllByCategory(category, pageable);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void createRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.save(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.createRecipe(DtoMockData.generateRandomMockDataForRecipeCreateRequest()));

        verify(mockRecipeRepository).save(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void createRecipe_SaveSuccessful_ReturnModel() {
        when(mockRecipeRepository.save(any())).thenReturn(RecipeMockData.generateRandomMockData(1).get(0));

        assertNotNull(recipeService.createRecipe(DtoMockData.generateRandomMockDataForRecipeCreateRequest()));

        verify(mockRecipeRepository).save(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void deleteRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.deleteRecipe(null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void deleteRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.deleteRecipe(19L));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void deleteRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.deleteRecipe(11L));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void deleteRecipe_AuthenticationServiceThrowsException_PropagateException() {
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
    void deleteRecipe_UserIsNotTheAuthorOfRecipe_ThrowUnauthorizedActionException() {
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
    void deleteRecipe_DeleteSuccessful() {
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
    void updateRecipe_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(null, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));
        assertThrows(IllegalArgumentException.class, () -> recipeService.updateRecipe(1L, null));

        verifyNoInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void updateRecipe_RepositoryThrowsException_PropagateException() {
        when(mockRecipeRepository.findById(any())).thenThrow(new ConcurrencyFailureException("test"));

        assertThrows(DataAccessException.class, () -> recipeService.updateRecipe(19L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(any());
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void updateRecipe_RepositoryReturnsEmpty_ThrowDataDoesNotExistsException() {
        when(mockRecipeRepository.findById(11L)).thenReturn(Optional.empty());

        assertThrows(DataDoesNotExistException.class, () -> recipeService.updateRecipe(11L, DtoMockData.generateRandomMockDataForRecipeUpdateRequest()));

        verify(mockRecipeRepository).findById(11L);
        verifyNoMoreInteractions(mockRecipeRepository);
        verifyNoInteractions(mockAuthenticationService);
    }

    @Test
    void updateRecipe_AuthenticationServiceThrowsException_PropagateException() {
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
    void updateRecipe_UserIsNotTheAuthorOfRecipe_ThrowUnauthorizedActionException() {
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
    void updateRecipe_UpdateSuccessful_ReturnModel() {
        Recipe recipe = RecipeMockData.generateRandomMockData(1).get(0);
        RecipeUpdateRequest updateRequest = DtoMockData.generateRandomMockDataForRecipeUpdateRequest();

        mockRecipeMapper.update(updateRequest, recipe);

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

    @Test
    void getRecipes_InvalidPageableGiven_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipes(Pageable.unpaged()));
        assertEquals("getRecipes.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());

        t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipes(Pageable.ofSize(110)));
        assertEquals("getRecipes.pageable: Pageable can not be null, un paged or value greater than 50", t.getMessage());
    }

    @Test
    void getRecipe_NegativeRecipeId_ThrowValidationException() {
        Throwable t = assertThrows(ConstraintViolationException.class, () -> recipeService.getRecipe(-1L));
        assertEquals("getRecipe.id: must be between 1 and 9223372036854775807", t.getMessage());
    }

    //TODO validation checks
}
