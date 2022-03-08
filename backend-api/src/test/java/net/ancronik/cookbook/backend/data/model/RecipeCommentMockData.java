package net.ancronik.cookbook.backend.data.model;

import net.ancronik.cookbook.backend.StringTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeCommentMockData {

    public static List<RecipeComment> generateRandomMockData(int size) {
        List<RecipeComment> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            RecipeComment entity = new RecipeComment(
                    new RecipeCommentPK(
                            (long) random.nextInt(Integer.MAX_VALUE),
                            StringTestUtils.getRandomStringInLowerCase(random.nextInt(20)),
                            LocalDateTime.now().minusDays(random.nextInt(100) + 1)
                    ),
                    StringTestUtils.getRandomStringInLowerCase(random.nextInt(200))
            );

            data.add(entity);
        }

        return data;
    }

}
