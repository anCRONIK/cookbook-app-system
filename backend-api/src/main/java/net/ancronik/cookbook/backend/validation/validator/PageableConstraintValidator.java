package net.ancronik.cookbook.backend.validation.validator;

import net.ancronik.cookbook.backend.validation.annotation.PageableConstraint;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for {@link Pageable} to check page size.
 * <p>
 * Given pageable can not be null or unpaged.
 *
 * @author Nikola Presecki
 */
public class PageableConstraintValidator implements ConstraintValidator<PageableConstraint, Pageable> {

    public static final int MAX_PAGE_SIZE = 50; //value related to spring.data.web.pageable.max-page-size

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext constraintValidatorContext) {

        return null != pageable && pageable.isPaged() && pageable.getPageSize() < MAX_PAGE_SIZE;
    }
}
