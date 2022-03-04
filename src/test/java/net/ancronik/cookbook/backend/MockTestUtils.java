package net.ancronik.cookbook.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockTestUtils {

    public static <T> List<T> convertMatrixToList(T[][] matrix) {
        List<T> list = new ArrayList<T>();
        for (T[] array : matrix) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

}
