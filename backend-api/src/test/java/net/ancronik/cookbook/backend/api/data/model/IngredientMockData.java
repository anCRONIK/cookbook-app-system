package net.ancronik.cookbook.backend.api.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.api.StringTestUtils.getRandomStringInLowerCase;

public class IngredientMockData {


    public static List<Ingredient> generateRandomMockData(int size) {
        List<Ingredient> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new Ingredient(
                    getRandomStringInLowerCase(random.nextInt(100)+1),
                    "" + random.nextInt(100), getRandomStringInLowerCase(4)+1)
            );
        }

        return data;
    }

}
