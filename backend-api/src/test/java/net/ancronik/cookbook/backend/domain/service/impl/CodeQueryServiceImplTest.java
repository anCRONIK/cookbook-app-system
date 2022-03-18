package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.domain.assembler.MeasurementUnitModelAssembler;
import net.ancronik.cookbook.backend.domain.service.impl.CodeQueryServiceImpl;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCategoryModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag(TestTypes.UNIT)
public class CodeQueryServiceImplTest {

    private final MeasurementUnitRepository mockMeasurementUnitRepository = Mockito.mock(MeasurementUnitRepository.class);

    private final RecipeCategoryRepository mockRecipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);

    private final MeasurementUnitModelAssembler measurementUnitModelAssembler = new MeasurementUnitModelAssembler(new ModelMapper());

    private final CodeQueryServiceImpl service = new CodeQueryServiceImpl(mockMeasurementUnitRepository, mockRecipeCategoryRepository, measurementUnitModelAssembler);

    @Test
    public void getMeasurementUnits_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockMeasurementUnitRepository).findAll();

        assertThrows(DataAccessException.class, service::getMeasurementUnits);

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    public void getMeasurementUnits_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getMeasurementUnits().isEmpty());

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    public void getMeasurementUnits_RepositoryReturnsList_ReturnData() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(MeasurementUnitMockData.generateRandomMockData(10));

        List<MeasurementUnitModel> data = service.getMeasurementUnits();

        assertNotNull(data);
        assertEquals(10, data.size());

        verify(mockMeasurementUnitRepository).findAll();
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    public void isMeasurementUnitValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isMeasurementUnitValid(null));

        verifyNoInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    public void isMeasurementUnitValid_EmptyStringGiven_ReturnTrueAndDoNotUseRepository() {
        assertTrue(service.isMeasurementUnitValid(""));

        verifyNoInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @Test
    public void isMeasurementUnitValid_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockMeasurementUnitRepository).existsById(anyString());

        assertThrows(DataAccessException.class, () -> service.isMeasurementUnitValid("uiodsa"));

        verify(mockMeasurementUnitRepository).existsById(anyString());
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    public void isMeasurementUnitValid_UnitDoesNotExists_ReturnFalse() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isMeasurementUnitValid("kgsa"));

        verify(mockMeasurementUnitRepository).existsById("kgsa");
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }

    @SneakyThrows
    @Test
    public void isMeasurementUnitValid_UnitExists_ReturnTrue() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isMeasurementUnitValid("kg"));

        verify(mockMeasurementUnitRepository).existsById("kg");
        verifyNoMoreInteractions(mockMeasurementUnitRepository);
        verifyNoInteractions(mockRecipeCategoryRepository);
    }


    @Test
    public void getRecipeCategories_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockRecipeCategoryRepository).findAll();

        assertThrows(DataAccessException.class, service::getRecipeCategories);

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    public void getRecipeCategories_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockRecipeCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getRecipeCategories().isEmpty());

        verify(mockRecipeCategoryRepository).findAll();
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    public void getRecipeCategories_RepositoryReturnsList_ReturnData() {
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
    public void isRecipeCategoryValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isRecipeCategoryValid(null));

        verifyNoInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockRecipeCategoryRepository).existsById(anyString());

        assertThrows(DataAccessException.class, () -> service.isRecipeCategoryValid("uiodsa"));

        verify(mockRecipeCategoryRepository).existsById(anyString());
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_UnitDoesNotExists_ReturnFalse() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isRecipeCategoryValid("kgsa"));

        verify(mockRecipeCategoryRepository).existsById("kgsa");
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_UnitExists_ReturnTrue() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isRecipeCategoryValid("entree"));

        verify(mockRecipeCategoryRepository).existsById("entree");
        verifyNoMoreInteractions(mockRecipeCategoryRepository);
        verifyNoInteractions(mockMeasurementUnitRepository);
    }

}
