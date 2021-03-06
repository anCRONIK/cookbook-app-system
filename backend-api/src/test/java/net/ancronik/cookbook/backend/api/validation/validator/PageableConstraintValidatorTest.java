package net.ancronik.cookbook.backend.api.validation.validator;

import net.ancronik.cookbook.backend.api.TestTypes;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag(TestTypes.UNIT)
class PageableConstraintValidatorTest {

    PageableConstraintValidator validator = new PageableConstraintValidator();

    @Test
    void isValid_MultipleTests() {
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid(Pageable.unpaged(), null));
        assertFalse(validator.isValid(Pageable.ofSize(111), null));
        assertTrue(validator.isValid(Pageable.ofSize(10), null));
    }

}
