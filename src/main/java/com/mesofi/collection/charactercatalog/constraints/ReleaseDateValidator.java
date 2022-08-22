package com.mesofi.collection.charactercatalog.constraints;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, Date> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value)) {
            // The release date should not be less than 2003-11-01 or greater than 6 months from now",
            Calendar calendar = Calendar.getInstance();
            calendar.set(2003, 10, 01);

            if (value.before(calendar.getTime())) {
                return false;
            }
            calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, calendar.get(Calendar.MONTH) + 6);

            if (value.after(calendar.getTime())) {
                return false;
            }

            return true;
        }
        return false;
    }
}
