package net.ancronik.cookbook.backend.authentication.validation.validator;

import net.ancronik.cookbook.backend.authentication.validation.annotation.CombinedNotNull;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for our annotation {@link CombinedNotNull}.
 *
 * @author Nikola Presecki
 */
public class CombinedNotNullValidator implements ConstraintValidator<CombinedNotNull, Object> {

    private String[] fields;

    @Override
    public void initialize(final CombinedNotNull combinedNotNull) {
        fields = combinedNotNull.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(object);

        for (final String f : fields) {
            if (null != beanWrapper.getPropertyValue(f)) {
                return true;
            }
        }

        return false;
    }
}
