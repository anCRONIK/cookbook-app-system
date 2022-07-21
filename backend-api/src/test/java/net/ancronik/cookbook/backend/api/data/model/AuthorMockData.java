package net.ancronik.cookbook.backend.api.data.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.ancronik.cookbook.backend.api.StringTestUtils.generateRandomUrl;
import static net.ancronik.cookbook.backend.api.StringTestUtils.getRandomStringInLowerCase;

public class AuthorMockData {

    public static List<Author> generateRandomMockData(int size) {
        List<Author> data = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            data.add(new Author(
                         getRandomStringInLowerCase(random.nextInt(10) + 2),
                         getRandomStringInLowerCase(random.nextInt(20)),
                         LocalDate.now().minusMonths(random.nextInt(100)),
                         getRandomStringInLowerCase(random.nextInt(20)),
                         generateRandomUrl()
                     )
            );
        }

        return data;
    }

}
