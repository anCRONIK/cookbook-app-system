package net.ancronik.cookbook.backend;

import java.util.Random;

public class StringTestUtils {

    private static final int LOWERCASE_A = 97; // letter 'a'
    private static final int LOWERCASE_Z = 122; // letter 'z'
    private static final String[] WEB_PROTOCOLS = new String[]{"http", "https", "ftp"};
    private static final String[] WEB_DOMAINS = new String[]{"com", "net", "hr", "site"};
    private static final String[] IMG_EXTENSIONS = new String[]{"png", "jpg", "jpeg", "svg"};
    public static Random random = new Random();

    public static String getRandomStringInLowerCase(int length) {
        return random.ints(LOWERCASE_A, LOWERCASE_Z + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateRandomUrl() {
        return String.format("%s://%s.%s/%s/%s.%s",
                WEB_PROTOCOLS[random.nextInt(WEB_PROTOCOLS.length)],
                getRandomStringInLowerCase(random.nextInt(20) + 5),
                WEB_DOMAINS[random.nextInt(WEB_DOMAINS.length)],
                getRandomStringInLowerCase(random.nextInt(10) + 2),
                getRandomStringInLowerCase(random.nextInt(8) + 2),
                IMG_EXTENSIONS[random.nextInt(IMG_EXTENSIONS.length)]
        );
    }

}
