package com.prophius.socialmediaservice.annotations.validators;

import com.prophius.socialmediaservice.annotations.DoesNotExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class DoesNotExistValidator implements ConstraintValidator<DoesNotExist, Object> {

    private final JdbcTemplate jdbcTemplate;
    private DoesNotExist doesNotExist;

    @Override
    public void initialize(DoesNotExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        doesNotExist = constraintAnnotation;
    }

    @Override
    @Cacheable(value = "isUniqueRecord", key = "#value", condition = "#result == false")
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (doesNotExist.nullable() && value == null) return true;
        if (value == null) return false;

        String table = doesNotExist.table();
        String columnName = doesNotExist.columnName();
        String query = doesNotExist.query();

        if (query == null || query.trim().isEmpty()) {
            query = "SELECT COUNT(*) FROM %s WHERE %s = ?";
            query = String.format(query, table, columnName);
        }

        int count = Optional.ofNullable(jdbcTemplate.queryForObject(query, Integer.class, value))
                .orElse(0);
        return count == 0;
    }
}
