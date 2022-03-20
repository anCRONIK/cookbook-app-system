package net.ancronik.cookbook.backend.api.domain.service.impl;

import net.ancronik.cookbook.backend.api.TestConfigurationForUnitTesting;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.api.data.model.RecipeCategory;
import net.ancronik.cookbook.backend.api.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCategoryRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationForUnitTesting.class)
@Tag(TestTypes.UNIT)
public class CodeQueryServiceImplCacheableTest {

    @Autowired
    private MeasurementUnitRepository mockMeasurementUnitRepository;

    @Autowired
    private RecipeCategoryRepository mockRecipeCategoryRepository;

    @Autowired
    private CodeQueryServiceImpl service;

    @Test
    public void getMeasurementUnitsSAndIsMeasurementUnitValid_TestCacheable() {
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
    public void getRecipeCategoriesAndIsRecipeCategoryValid_TestCacheable() {
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
