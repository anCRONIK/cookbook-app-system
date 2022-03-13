package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.SneakyThrows;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.domain.assembler.MeasurementUnitModelAssembler;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
    }

    @SneakyThrows
    @Test
    public void getMeasurementUnits_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getMeasurementUnits().isEmpty());
    }

    @SneakyThrows
    @Test
    public void getMeasurementUnits_RepositoryReturnsList_ReturnData() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(MeasurementUnitMockData.generateRandomMockData(10));

        List<MeasurementUnitModel> data = service.getMeasurementUnits();

        assertNotNull(data);
        assertEquals(10, data.size());
    }

    @Test
    public void isMeasurementUnitValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isMeasurementUnitValid(null));
    }

    @Test
    public void isMeasurementUnitValid_RepositoryThrowsException_PropagateException() {
        doThrow(new ConcurrencyFailureException("test")).when(mockMeasurementUnitRepository).existsById(anyString());

        assertThrows(DataAccessException.class, () -> service.isMeasurementUnitValid("uiodsa"));
    }

    @SneakyThrows
    @Test
    public void isMeasurementUnitValid_UnitDoesNotExists_ReturnFalse() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isMeasurementUnitValid("kgsa"));
    }

    @SneakyThrows
    @Test
    public void isMeasurementUnitValid_UnitExists_ReturnTrue() {
        when(mockMeasurementUnitRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isMeasurementUnitValid("kg"));
    }


    @Test
    public void getRecipeCategories_RepositoryThrowsException_PropagateException() {
        doThrow(new RuntimeException("test")).when(mockRecipeCategoryRepository).findAll();

        assertThrows(RuntimeException.class, service::getRecipeCategories);
    }

    @SneakyThrows
    @Test
    public void getRecipeCategories_RepositoryReturnsEmptyList_ReturnEmptyList() {
        when(mockRecipeCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getRecipeCategories().isEmpty());
    }

    @SneakyThrows
    @Test
    public void getRecipeCategories_RepositoryReturnsList_ReturnData() {
        when(mockRecipeCategoryRepository.findAll())
                .thenReturn(List.of(new RecipeCategory("dessert"), new RecipeCategory("entree")));

        List<RecipeCategoryModel> data = service.getRecipeCategories();

        assertNotNull(data);
        assertEquals(2, data.size());
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_NullGiven_ThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.isRecipeCategoryValid(null));
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_RepositoryThrowsException_PropagateException() {
        doThrow(new RuntimeException("test")).when(mockRecipeCategoryRepository).existsById(anyString());

        assertThrows(RuntimeException.class, () -> service.isRecipeCategoryValid("uiodsa"));
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_UnitDoesNotExists_ReturnFalse() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(false);

        assertFalse(service.isRecipeCategoryValid("kgsa"));
    }

    @SneakyThrows
    @Test
    public void isRecipeCategoryValid_UnitExists_ReturnTrue() {
        when(mockRecipeCategoryRepository.existsById(anyString())).thenReturn(true);

        assertTrue(service.isRecipeCategoryValid("entree"));
    }

}
