package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.api.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.api.domain.assembler.MeasurementUnitModelAssembler;
import net.ancronik.cookbook.backend.api.web.dto.recipe.MeasurementUnitModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCategoryModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag(TestTypes.UNIT)
class CodeQueryServiceImplTest {

    @Mock
    private MeasurementUnitRepository mockMeasurementUnitRepository;
    @Mock
    private RecipeCategoryRepository mockRecipeCategoryRepository;
    @Mock
    private MeasurementUnitModelAssembler measurementUnitModelAssembler;
    @InjectMocks
    private CodeQueryServiceImpl service;

    @Test
    void getMeasurementUnits_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockMeasurementUnitRepository).findAll();

        assertThrows(DataAccessException.class, service::getMeasurementUnits);

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    void getMeasurementUnits_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getMeasurementUnits().isEmpty());

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    void getMeasurementUnits_RepositoryReturnsList_ReturnData() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(MeasurementUnitMockData.generateRandomMockData(10));

        List<MeasurementUnitModel> data = service.getMeasurementUnits();

        assertNotNull(data);
        assertEquals(10, data.size());

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    void isMeasurementUnitValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isMeasurementUnitValid(null));

        verifyNoInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    void isMeasurementUnitValid_EmptyStringGiven_ReturnTrueAndDoNotUseRepository() {
        assertTrue(service.isMeasurementUnitValid(""));

        verifyNoInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    void isMeasurementUnitValid_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockMeasurementUnitRepository).existsById(anyString());

        assertThrows(DataAccessException.class, () -> service.isMeasurementUnitValid("uiodsa"));

        verify(mockMeasurementUnitRepository).existsById(anyString());
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    void isMeasurementUnitValid_UnitDoesNotExists_ReturnFalse() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isMeasurementUnitValid("kgsa"));

        verify(mockMeasurementUnitRepository).existsById("kgsa");
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    void isMeasurementUnitValid_UnitExists_ReturnTrue() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isMeasurementUnitValid("kg"));

        verify(mockMeasurementUnitRepository).existsById("kg");
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }


    @Test
    void getRecipeCategories_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockRecipeCategoryRepository).findAll();

        assertThrows(DataAccessException.class, service::getRecipeCategories);

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void getRecipeCategories_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockRecipeCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getRecipeCategories().isEmpty());

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void getRecipeCategories_RepositoryReturnsList_ReturnData() {
        when(mockRecipeCategoryRepository.findAll())
            .thenReturn(List.of(new RecipeCategory("dessert"), new RecipeCategory("entree")));

        List<RecipeCategoryModel> data = service.getRecipeCategories();

        assertNotNull(data);
        assertEquals(2, data.size());

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void isRecipeCategoryValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isRecipeCategoryValid(null));

        verifyNoInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void isRecipeCategoryValid_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockRecipeCategoryRepository).existsById(anyString());

        assertThrows(DataAccessException.class, () -> service.isRecipeCategoryValid("uiodsa"));

        verify(mockRecipeCategoryRepository).existsById(anyString());
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void isRecipeCategoryValid_UnitDoesNotExists_ReturnFalse() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isRecipeCategoryValid("kgsa"));

        verify(mockRecipeCategoryRepository).existsById("kgsa");
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    void isRecipeCategoryValid_UnitExists_ReturnTrue() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isRecipeCategoryValid("entree"));

        verify(mockRecipeCategoryRepository).existsById("entree");
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @Test
    void getMeasurementUnitsSAndIsMeasurementUnitValid_TestCacheable() {
        String unit1 = "";
        String unit2 = "spoon";
        String unit3 = "none";
        when(mockMeasurementUnitRepository.existsById(unit1)).thenThrow(new RuntimeException("this shouldn't happen"));
        when(mockMeasurementUnitRepository.existsById(unit2)).thenReturn(true);
        when(mockMeasurementUnitRepository.existsById(unit3)).thenReturn(false);
        when(mockMeasurementUnitRepository.findAll()).thenReturn(MeasurementUnitMockData.generateRandomMockData(10));

        service.getMeasurementUnits();

        assertTrue(service.isMeasurementUnitValid(unit1));
        assertTrue(service.isMeasurementUnitValid(unit2));
        service.getMeasurementUnits();
        assertFalse(service.isMeasurementUnitValid(unit3));
        assertFalse(service.isMeasurementUnitValid(unit3));
        assertTrue(service.isMeasurementUnitValid(unit2));
        assertTrue(service.isMeasurementUnitValid(unit2));
        service.getMeasurementUnits();
        assertTrue(service.isMeasurementUnitValid(unit2));
        assertTrue(service.isMeasurementUnitValid(unit1));

        verify(mockMeasurementUnitRepository).existsById(unit2);
        verify(mockMeasurementUnitRepository).existsById(unit3);

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
    }

    @Test
    void getRecipeCategoriesAndIsRecipeCategoryValid_TestCacheable() {
        String category1 = "";
        String category2 = "entree";
        String category3 = "random";
        when(mockRecipeCategoryRepository.existsById(category1)).thenReturn(false);
        when(mockRecipeCategoryRepository.existsById(category2)).thenReturn(true);
        when(mockRecipeCategoryRepository.existsById(category3)).thenReturn(false);
        when(mockRecipeCategoryRepository.findAll()).thenReturn(List.of(new RecipeCategory("dessert"), new RecipeCategory("entree")));

        service.getRecipeCategories();

        assertTrue(service.isRecipeCategoryValid(category2));
        assertThrows(ConstraintViolationException.class, () -> service.isRecipeCategoryValid(category1));
        assertFalse(service.isRecipeCategoryValid(category3));
        service.getRecipeCategories();
        assertFalse(service.isRecipeCategoryValid(category3));
        assertTrue(service.isRecipeCategoryValid(category2));
        service.getRecipeCategories();
        assertTrue(service.isRecipeCategoryValid(category2));
        assertTrue(service.isRecipeCategoryValid(category2));

        verify(mockRecipeCategoryRepository).existsById(category2);
        verify(mockRecipeCategoryRepository).existsById(category3);

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
    }
}
