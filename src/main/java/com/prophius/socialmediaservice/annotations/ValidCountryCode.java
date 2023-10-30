package com.prophius.socialmediaservice.annotations;

import com.prophius.socialmediaservice.annotations.validators.CountryCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface ValidCountryCode {
    String message() default "country_code.invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
