package net.ancronik.cookbook.backend.domain.assembler;

import lombok.NonNull;
import net.ancronik.cookbook.backend.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.web.controller.MeasurementUnitController;
import net.ancronik.cookbook.backend.web.dto.MeasurementUnitDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Assembler for {@link net.ancronik.cookbook.backend.web.dto.MeasurementUnitDto} from {@link net.ancronik.cookbook.backend.data.model.MeasurementUnit}.
 *
 * @author Nikola Presecki
 */
@Component
public class MeasurementUnitDtoAssembler extends RepresentationModelAssemblerSupport<MeasurementUnit, MeasurementUnitDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementUnitDtoAssembler(ModelMapper modelMapper) {
        super(MeasurementUnitController.class, MeasurementUnitDto.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public MeasurementUnitDto toModel(@NonNull MeasurementUnit entity) {
        return modelMapper.map(entity, MeasurementUnitDto.class);
    }
}
