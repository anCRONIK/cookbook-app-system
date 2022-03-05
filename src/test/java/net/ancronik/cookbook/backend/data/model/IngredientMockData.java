package net.ancronik.cookbook.backend.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.StringTestUtils.getRandomStringInLowerCase;

public class IngredientMockData {


    public static List<Ingredient> generateRandomMockData(int size) {
        List<Ingredient> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new Ingredient(
                    getRandomStringInLowerCase(random.nextInt(100)),
                    new IngredientQuantity("" + random.nextInt(100), MeasurementUnit.values()[random.nextInt(MeasurementUnit.values().length)]))
            );
        }

        return data;
    }

}
