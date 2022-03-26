package net.ancronik.cookbook.backend.integrationtest.data.repository;

import net.ancronik.cookbook.backend.api.CookbookBackendApiSpringBootApp;
import net.ancronik.cookbook.backend.integrationtest.CassandraTestContainersExtension;
import net.ancronik.cookbook.backend.api.TestTypes;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.api.data.repository.MeasurementUnitRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CookbookBackendApiSpringBootApp.class)
@ExtendWith({SpringExtension.class, CassandraTestContainersExtension.class})
@Tag(TestTypes.INTEGRATION)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MeasurementUnitRepositoryIT {

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
