package net.ancronik.cookbook.backend.data.model;

import net.ancronik.cookbook.backend.StringTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeMockData {

    public static List<Recipe> generateRandomMockData(int size) {
        List<Recipe> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            Recipe entity = new Recipe(
                    (long) random.nextInt(Integer.MAX_VALUE),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(100)),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(200)),
                    random.nextBoolean() ? StringTestUtils.generateRandomUrl() : null,
                    random.nextBoolean() ? StringTestUtils.generateRandomUrl() : null,
                    IngredientMockData.generateRandomMockData(random.nextInt(20)),
                    random.nextInt(300),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(1000)),
                    random.nextInt(600),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(200)+2),
                    LocalDateTime.now().minusDays(random.nextInt(100) + 1),
                    random.nextBoolean() ? LocalDateTime.now() : null,
                    random.nextInt(5) + 1,
                    new RecipeCategory(StringTestUtils.getRandomStringInLowerCase(6)+1),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(10)+2)
            );

            data.add(entity);
        }

        return data;
    }

}
