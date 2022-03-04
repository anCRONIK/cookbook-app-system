package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.StringTestUtils;
import net.ancronik.cookbook.backend.data.model.RecipeCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeDtoMockData {


    public static List<RecipeDto> generateRandomMockData(int size) {
        List<RecipeDto> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new RecipeDto(
                    (long) random.nextInt(Integer.MAX_VALUE),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(100)),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(200)),
                    random.nextBoolean() ? StringTestUtils.generateRandomUrl() : null,
                    IngredientDtoMockData.generateRandomMockData(random.nextInt(20)),
                    random.nextInt(300),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(1000)),
                    random.nextInt(600),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(1000)),
                    LocalDateTime.now().minusDays(random.nextInt(100) + 1),
                    random.nextBoolean() ? LocalDateTime.now() : null,
                    random.nextInt(5),
                    RecipeCategory.values()[random.nextInt(RecipeCategory.values().length)].getCategory(),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(10))
            ));
        }

        return data;
    }

}
