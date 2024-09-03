package com.app.photographers.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventTypeIdNotNullValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTypeIdNotNull {
    String message() default "Event type ID is required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}