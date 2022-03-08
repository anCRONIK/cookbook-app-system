package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.domain.assembler.MeasurementUnitDtoAssembler;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitDto;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CodeQueryService}.
 */
@Service
@Slf4j
public class CodeQueryServiceImpl implements CodeQueryService {

    private final MeasurementUnitRepository measurementUnitRepository;

    private final RecipeCategoryRepository recipeCategoryRepository;

    private final MeasurementUnitDtoAssembler measurementUnitDtoAssembler;

    @Autowired
    public CodeQueryServiceImpl(MeasurementUnitRepository measurementUnitRepository, RecipeCategoryRepository recipeCategoryRepository, MeasurementUnitDtoAssembler measurementUnitDtoAssembler) {
        this.measurementUnitRepository = measurementUnitRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.measurementUnitDtoAssembler = measurementUnitDtoAssembler;
    }

    @Cacheable(value = "measurement_units")
    @Override
    public List<MeasurementUnitDto> getMeasurementUnits() throws GenericDatabaseException {
        try {
            return measurementUnitRepository.findAll().stream().map(measurementUnitDtoAssembler::toModel).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while fetching measurement units from database", e);
            throw new GenericDatabaseException(e);
        }
    }

    @Cacheable(value = "measurement_units", key = "unit")
    @Override
    public boolean isMeasurementUnitValid(String unit) throws GenericDatabaseException {
        try {
            return measurementUnitRepository.existsById(unit);
        } catch (Exception e) {
            LOG.error("Error while checking if unit exists [{}]", unit, e);
            throw new GenericDatabaseException(e);
        }
    }

    @Cacheable(value = "recipe_categories")
    @Override
    public List<RecipeCategoryDto> getRecipeCategories() throws GenericDatabaseException {
        try {
            return recipeCategoryRepository.findAll().stream().map(c -> new RecipeCategoryDto(c.getCategory())).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while fetching recipe categories from database", e);
            throw new GenericDatabaseException(e);
        }
    }

    @Cacheable(value = "recipe_categories", key = "category")
    @Override
    public boolean isRecipeCategoryValid(String category) throws GenericDatabaseException {
        try {
            return recipeCategoryRepository.existsById(category);
        } catch (Exception e) {
            LOG.error("Error while checking if category exists [{}]", category, e);
            throw new GenericDatabaseException(e);
        }
    }
}
