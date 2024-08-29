package com.app.photographers.service;

import com.app.photographers.model.EventType;
import jakarta.validation.Valid;

public interface EventTypeService {

        void saveEventType(@Valid EventType eventType);

        Iterable<EventType> getAllEventTypes();

        EventType getEventTypeById(Long id);

        void deleteEventType(Long id);

        EventType updateEventType(Long id, EventType eventTypes);
}
