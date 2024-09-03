package com.app.photographers.validation;

import com.app.photographers.model.EventType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventTypeIdNotNullValidator implements ConstraintValidator<EventTypeIdNotNull, EventType> {

    @Override
    public boolean isValid(EventType eventType, ConstraintValidatorContext context) {
        return eventType != null && eventType.getId() != null;
    }
}