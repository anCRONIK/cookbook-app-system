package net.ancronik.cookbook.backend.authentication.validation.annotation;

import net.ancronik.cookbook.backend.authentication.validation.validator.CombinedNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that can be used to validate multiple fields that are optional, but one of them must not be null.
 *
 * @author Nikola Presecki
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = CombinedNotNullValidator.class)
public @interface CombinedNotNull {
    String message() default "one of the fields must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields() default {};
}