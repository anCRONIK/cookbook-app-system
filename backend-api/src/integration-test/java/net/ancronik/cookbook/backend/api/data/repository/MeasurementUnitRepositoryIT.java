package net.ancronik.cookbook.backend.api.data.repository;

import net.ancronik.cookbook.backend.api.DatabaseIntegrationTest;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MeasurementUnitRepositoryIT extends DatabaseIntegrationTest {

    @Autowired
    MeasurementUnitRepository measurementUnitRepository;

    @Test
    public void readEntries_AllEntriesFromDatabaseShouldBeLoaded() {
        List<MeasurementUnit> list = measurementUnitRepository.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 2);
    }

    @Test
    public void findById_MultipleTests() {
        assertTrue(measurementUnitRepository.findById("test").isEmpty());
        assertTrue(measurementUnitRepository.findById("kg").isPresent());
    }

    @Test
    public void testThatWriteOperationsAreNotSupported() {
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.save(new MeasurementUnit("test", "test", "weight", false, true)));
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.delete(new MeasurementUnit("test", "test", "weight", false, true)));
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.saveAll(List.of(new MeasurementUnit("test", "test", "weight", false, true))));
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.deleteAll(List.of(new MeasurementUnit("test", "test", "weight", false, true))));
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.deleteAll());
        assertThrows(UnsupportedOperationException.class, () -> measurementUnitRepository.deleteAllById(List.of("test")));
    }

}
