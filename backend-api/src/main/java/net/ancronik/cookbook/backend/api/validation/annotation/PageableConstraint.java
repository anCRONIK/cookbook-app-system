package net.ancronik.cookbook.backend.api.validation.annotation;

import net.ancronik.cookbook.backend.api.validation.validator.PageableConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = PageableConstraintValidator.class)
public @interface PageableConstraint {

    String message() default
        "Pageable can not be null, un paged or value greater than " + PageableConstraintValidator.MAX_PAGE_SIZE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
