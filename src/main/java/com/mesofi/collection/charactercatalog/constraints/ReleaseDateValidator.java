package com.mesofi.collection.charactercatalog.constraints;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, Date> {

    private boolean failOnFutureDate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        failOnFutureDate = constraintAnnotation.failOnFutureDate();
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            // if the annotation is present but the value is null.
            return false;
        } else {
            // The release date should not be less than 2003-11-01 or greater than 6 months from now".
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.set(2003, 10, 01);

            // before 2003-11-01 ??
            if (value.before(calendarStart.getTime())) {
                return false;
            }

            Calendar calendarToday = Calendar.getInstance();
            if (failOnFutureDate) {
                if (value.after(calendarToday.getTime())) {
                    return false;
                }
            } else {
                calendarToday.add(Calendar.MONTH, 6);

                // after 6 months from now
                if (value.after(calendarToday.getTime())) {
                    return false;
                }
            }

            // all is good
            return true;
        }
    }
}
