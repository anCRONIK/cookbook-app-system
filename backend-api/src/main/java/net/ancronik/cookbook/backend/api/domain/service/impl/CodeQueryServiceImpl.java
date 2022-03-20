package net.ancronik.cookbook.backend.api.domain.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.api.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.api.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.api.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.api.web.dto.recipe.MeasurementUnitModel;
import net.ancronik.cookbook.backend.api.web.dto.recipe.RecipeCategoryModel;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
        LOG.info("Fetching all measurement units");

        return measurementUnitRepository.findAll().stream().map(measurementUnitModelAssembler::toModel).collect(Collectors.toList());
    }

    @Cacheable(value = "measurement_units")
    @Override
    public boolean isMeasurementUnitValid(@NonNull String unit) {
        LOG.info("Checking if measurement unit is valid [{}]", unit);

        return unit.isEmpty() || measurementUnitRepository.existsById(unit);
    }

    @Cacheable(value = "recipe_categories")
    @Override
    public List<RecipeCategoryModel> getRecipeCategories() {
        return recipeCategoryRepository.findAll().stream().map(c -> new RecipeCategoryModel(c.getCategory())).collect(Collectors.toList());
    }

    @Cacheable(value = "recipe_categories")
    @Override
    public boolean isRecipeCategoryValid(@NonNull @NotBlank @CodePointLength(max = 50) String category) {
        LOG.info("Checking if category is valid [{}]", category);

        return recipeCategoryRepository.existsById(category);
    }
}
