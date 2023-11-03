package com.prophius.socialmediaservice.annotations;

import com.prophius.socialmediaservice.annotations.validators.DoesNotExistValidator;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DoesNotExistValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface DoesNotExist {
    String message() default "Non-unique record";

    String table();

    String columnName();

    String query() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;
}
