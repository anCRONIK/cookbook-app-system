package net.ancronik.cookbook.backend.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.ancronik.cookbook.backend.domain.service.CodeQueryService;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling all operations for the {@link net.ancronik.cookbook.backend.data.model.MeasurementUnit}.
 *
 * @author Nikola Presecki
 */
@RestController
@RequestMapping(MeasurementUnitController.DEFAULT_MAPPING)
@Slf4j
public class MeasurementUnitController {

    public static final String DEFAULT_MAPPING = "/api/v1/measurement-units";

    private final CodeQueryService codeQueryService;

    @Autowired
    public MeasurementUnitController(CodeQueryService codeQueryService) {
        this.codeQueryService = codeQueryService;
    }

    @GetMapping(value = "", produces = MediaTypes.HAL_JSON_VALUE)
    @Cacheable("measurement_units") //TODO add header for client to cache data for an hour
    public CollectionModel<MeasurementUnitModel> getMeasurementUnits() {
        LOG.debug("Fetching all measurement units");

        return CollectionModel.of(codeQueryService.getMeasurementUnits());
    }
}
