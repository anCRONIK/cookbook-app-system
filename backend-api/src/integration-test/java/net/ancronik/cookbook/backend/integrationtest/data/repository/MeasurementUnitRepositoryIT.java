package net.ancronik.cookbook.backend.integrationtest.data.repository;

import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.api.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.integrationtest.BaseIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasurementUnitRepositoryIT extends BaseIntegrationTest {

    @Autowired
    MeasurementUnitRepository measurementUnitRepository;

    @Test
    void readEntries_AllEntriesFromDatabaseShouldBeLoaded() {
        List<MeasurementUnit> list = measurementUnitRepository.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 2);
    }

    @Test
    void findById_MultipleTests() {
        assertTrue(measurementUnitRepository.findById("test").isEmpty());
        assertTrue(measurementUnitRepository.findById("kg").isPresent());
    }

}
