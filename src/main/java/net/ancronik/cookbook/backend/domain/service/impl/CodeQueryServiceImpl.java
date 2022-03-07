package net.ancronik.cookbook.backend.domain.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.domain.assembler.MeasurementUnitDtoAssembler;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.web.dto.MeasurementUnitDto;
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

    private final MeasurementUnitDtoAssembler measurementUnitDtoAssembler;

    @Autowired
    public CodeQueryServiceImpl(MeasurementUnitRepository measurementUnitRepository, MeasurementUnitDtoAssembler measurementUnitDtoAssembler) {
        this.measurementUnitRepository = measurementUnitRepository;
        this.measurementUnitDtoAssembler = measurementUnitDtoAssembler;
    }

    @Cacheable("measurement_units")
    @Override
    public List<MeasurementUnitDto> getMeasurementUnits() throws GenericDatabaseException {
        try {
            return measurementUnitRepository.findAll().stream().map(measurementUnitDtoAssembler::toModel).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("Error while fetching measurement units from database", e);
            throw new GenericDatabaseException(e);
        }
    }

    @Cacheable("measurement_units")
    @Override
    public boolean isMeasurementUnitValid(String unit) throws GenericDatabaseException {
        try {
            return measurementUnitRepository.existsById(unit);
        } catch (Exception e) {
            LOG.error("Error while checking if unit exists [{}]", unit, e);
            throw new GenericDatabaseException(e);
        }
    }
}
