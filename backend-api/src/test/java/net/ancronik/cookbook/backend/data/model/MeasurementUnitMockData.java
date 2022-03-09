package net.ancronik.cookbook.backend.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.StringTestUtils.getRandomStringInLowerCase;

public class MeasurementUnitMockData {

    public static List<MeasurementUnit> generateRandomMockData(int size) {
        List<MeasurementUnit> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new MeasurementUnit(
                    getRandomStringInLowerCase(random.nextInt(20)+1),
                    getRandomStringInLowerCase(random.nextInt(20)+1),
                    getRandomStringInLowerCase(random.nextInt(20)+1),
                    random.nextBoolean(),
                    random.nextBoolean())
            );
        }

        return data;
    }

}
