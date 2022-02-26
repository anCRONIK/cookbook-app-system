package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.data.model.MeasurementUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.StringTestUtils.getRandomStringInLowerCase;

public class IngredientDtoMockData {


    public static List<IngredientDto> generateRandomMockData(int size) {
        List<IngredientDto> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new IngredientDto(
                    getRandomStringInLowerCase(random.nextInt(100)),
                    "" + random.nextInt(100),
                    MeasurementUnit.values()[random.nextInt(MeasurementUnit.values().length)].getUnit()
            ));
        }

        return data;
    }

}
