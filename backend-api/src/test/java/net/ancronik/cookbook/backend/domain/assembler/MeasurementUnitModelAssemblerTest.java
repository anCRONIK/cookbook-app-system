package net.ancronik.cookbook.backend.domain.assembler;

import net.ancronik.cookbook.backend.TestTypes;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.web.dto.recipe.MeasurementUnitModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(TestTypes.UNIT)
public class MeasurementUnitModelAssemblerTest {

    MeasurementUnitModelAssembler assembler = new MeasurementUnitModelAssembler(new ModelMapper());

    @Test
    public void toModel_NullGiven_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> assembler.toModel(null));
    }


    @Test
    public void toModel_ValidDataGiven_ValidDtoReturned() {
        MeasurementUnit unit = new MeasurementUnit("kg unit", "kg", "weight", true, false);
        MeasurementUnit unit2 = new MeasurementUnit("spoon unit", "spoon", "volume", true, true);

        MeasurementUnitModel dto = assembler.toModel(unit);
        assertEquals("MeasurementUnitModel(code=kg, category=weight, isImperial=true, isMetric=false)", dto.toString());
        assertEquals(0, dto.getLinks().toList().size());

        MeasurementUnitModel dto2 = assembler.toModel(unit2);
        assertEquals("MeasurementUnitModel(code=spoon, category=volume, isImperial=true, isMetric=true)", dto2.toString());
        assertEquals(0, dto2.getLinks().toList().size());
    }

}
