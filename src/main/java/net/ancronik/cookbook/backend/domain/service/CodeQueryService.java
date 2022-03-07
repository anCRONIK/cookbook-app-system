package net.ancronik.cookbook.backend.domain.service;

import net.ancronik.cookbook.backend.application.exceptions.GenericDatabaseException;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.web.dto.MeasurementUnitDto;

import java.util.List;

/**
 * Service which should handle fetching all codes from db and caching them properly.
 *
 * @author Nikola Presecki
 */
public interface CodeQueryService {

    /**
     * Method for fetching all {@link MeasurementUnit};
     *
     * @return list of units
     */
    List<MeasurementUnitDto> getMeasurementUnits() throws GenericDatabaseException;

    /**
     * Method for checking if given measurement unit code is valid.
     *
     * @param unit unit
     * @return list of units
     */
    boolean isMeasurementUnitValid(String unit) throws GenericDatabaseException;
}
