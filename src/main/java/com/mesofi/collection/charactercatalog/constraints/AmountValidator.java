package com.mesofi.collection.charactercatalog.constraints;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<Amount, BigDecimal> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Amount constraintAnnotation) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }
        if (value.compareTo(new BigDecimal(0)) <= 0) {
            return false;
        }
        return true;
    }
}