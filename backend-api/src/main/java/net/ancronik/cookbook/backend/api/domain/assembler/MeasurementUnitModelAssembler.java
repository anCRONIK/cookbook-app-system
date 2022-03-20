package net.ancronik.cookbook.backend.api.domain.assembler;

import lombok.NonNull;
import net.ancronik.cookbook.backend.api.data.model.MeasurementUnit;
import net.ancronik.cookbook.backend.api.web.controller.MeasurementUnitController;
import net.ancronik.cookbook.backend.api.web.dto.recipe.MeasurementUnitModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Assembler for {@link MeasurementUnitModel} from {@link MeasurementUnit}.
 *
 * @author Nikola Presecki
 */
@Component
public class MeasurementUnitModelAssembler extends RepresentationModelAssemblerSupport<MeasurementUnit, MeasurementUnitModel> {

    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementUnitModelAssembler(ModelMapper modelMapper) {
        super(MeasurementUnitController.class, MeasurementUnitModel.class);
        this.modelMapper = modelMapper;
    }

    @Override
    public MeasurementUnitModel toModel(@NonNull MeasurementUnit entity) {
        return modelMapper.map(entity, MeasurementUnitModel.class);
    }
}
