package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitModel;
import net.ancronik.cookbook.backend.web.dto.recipe.RecipeCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CodeQueryService}.
 */
@Service
@Slf4j
@Validated
public class CodeQueryServiceImpl implements CodeQueryService {

    private final MeasurementUnitRepository measurementUnitRepository;

    private final RecipeCategoryRepository recipeCategoryRepository;

    private final RepresentationModelAssemblerSupport<MeasurementUnit, MeasurementUnitModel> measurementUnitModelAssembler;

    @Autowired
    public CodeQueryServiceImpl(MeasurementUnitRepository measurementUnitRepository, RecipeCategoryRepository recipeCategoryRepository,
                                RepresentationModelAssemblerSupport<MeasurementUnit, MeasurementUnitModel> measurementUnitModelAssembler) {
        this.measurementUnitRepository = measurementUnitRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.measurementUnitModelAssembler = measurementUnitModelAssembler;
    }

    @Cacheable(value = "measurement_units")
    @Override
    public List<MeasurementUnitModel> getMeasurementUnits() {
        try {
            return measurementUnitRepository.findAll().stream().map(measurementUnitModelAssembler::toModel).collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOG.error("Error while fetching measurement units from database", e);
            throw e;
        }
    }

    @Cacheable(value = "measurement_units", key = "unit")
    @Override
    public boolean isMeasurementUnitValid(@NonNull @NotBlank String unit) {
        try {
            return measurementUnitRepository.existsById(unit);
        } catch (DataAccessException e) {
            LOG.error("Error while checking if unit exists [{}]", unit, e);
            throw e;
        }
    }

    @Cacheable(value = "recipe_categories")
    @Override
    public List<RecipeCategoryModel> getRecipeCategories() {
        try {
            return recipeCategoryRepository.findAll().stream().map(c -> new RecipeCategoryModel(c.getCategory())).collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOG.error("Error while fetching recipe categories from database", e);
            throw e;
        }
    }

    @Cacheable(value = "recipe_categories", key = "category")
    @Override
    public boolean isRecipeCategoryValid(@NonNull @NotBlank String category) {
        try {
            return recipeCategoryRepository.existsById(category);
        } catch (DataAccessException e) {
            LOG.error("Error while checking if category exists [{}]", category, e);
            throw e;
        }
    }
}
