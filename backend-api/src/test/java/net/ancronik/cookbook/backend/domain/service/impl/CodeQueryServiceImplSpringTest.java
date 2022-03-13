package net.ancronik.cookbook.backend.domain.service.impl;

import net.ancronik.cookbook.backend.TestConfigurationForUnitTesting;
import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.data.model.MeasurementUnitMockData;
import net.ancronik.cookbook.backend.data.repository.MeasurementUnitRepository;
import net.ancronik.cookbook.backend.data.repository.RecipeCategoryRepository;
import net.ancronik.cookbook.backend.domain.assembler.MeasurementUnitModelAssembler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfigurationForUnitTesting.class)
@Tag(TestTypes.UNIT)
public class CodeQueryServiceImplSpringTest {

    private final MeasurementUnitRepository mockMeasurementUnitRepository = Mockito.mock(MeasurementUnitRepository.class);

    private final RecipeCategoryRepository mockRecipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);

    private final MeasurementUnitModelAssembler measurementUnitModelAssembler = new MeasurementUnitModelAssembler(new ModelMapper());

    private final CodeQueryServiceImpl service = new CodeQueryServiceImpl(mockMeasurementUnitRepository, mockRecipeCategoryRepository, measurementUnitModelAssembler);

    @Test
    public void getMeasurementUnits_TestCacheable() {
        when(mockMeasurementUnitRepository.findAll()).thenReturn(MeasurementUnitMockData.generateRandomMockData(10));

        service.getMeasurementUnits();
        service.getMeasurementUnits();
//TODO
//        verify(mockMeasurementUnitRepository).findAll();
  //      verifyNoMoreInteractions(mockMeasurementUnitRepository);
    }

}
