package net.ancronik.cookbook.backend.web.dto;

import net.ancronik.cookbook.backend.StringTestUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeDtoMockData {


    public static List<RecipeDto> generateRandomMockData(int size) {
        List<RecipeDto> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new RecipeDto(
                    random.nextInt(Integer.MAX_VALUE),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(100)),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(200)),
                    null, //TODO add url generator
                    IngredientDtoMockData.generateRandomMockData(random.nextInt(20)),
                    random.nextInt(300),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(1000)),
                    random.nextInt(600),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(1000)),
                    random.nextInt(5)
            ));
        }

        return data;
    }

}
