package net.ancronik.cookbook.backend;

import java.util.Random;

public class StringTestUtils {

    public static Random random = new Random();
    private static int LOWERCASE_A = 97; // letter 'a'
    private static int LOWERCASE_Z = 122; // letter 'z'

    public static String getRandomStringInLowerCase(int length) {
        return random.ints(LOWERCASE_A, LOWERCASE_Z + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
