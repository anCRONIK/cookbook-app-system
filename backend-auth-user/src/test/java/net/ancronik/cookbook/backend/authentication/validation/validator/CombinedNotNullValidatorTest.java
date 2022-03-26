package net.ancronik.cookbook.backend.authentication.validation.validator;

import lombok.Data;
import net.ancronik.cookbook.backend.authentication.TestTypes;
import net.ancronik.cookbook.backend.authentication.validation.annotation.CombinedNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(TestTypes.UNIT)
public class CombinedNotNullValidatorTest {


    @CombinedNotNull(
            fields = {"f1", "f2"}
    )
    @Data
    private static class TestModel {

        private String f1;

        private String f2;

    }


    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void isValid_MultipleScenarios() {
        assertFalse(validator.validate(new TestModel()).isEmpty());

        TestModel t1 = new TestModel();
        t1.setF1("data");
        assertTrue(validator.validate(t1).isEmpty());

        TestModel t2 = new TestModel();
        t2.setF2("data");
        assertTrue(validator.validate(t2).isEmpty());
    }

}
