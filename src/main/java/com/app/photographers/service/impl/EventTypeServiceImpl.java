package com.app.photographers.service.impl;

import com.app.photographers.exception.ErrorMessages;
import com.app.photographers.exception.ResourceAlreadyExistsException;
import com.app.photographers.exception.ResourceNotFoundException;
import com.app.photographers.model.EventType;
import com.app.photographers.repository.EventTypeRepository;
import com.app.photographers.service.EventTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    @Override
    public void saveEventType(@Valid EventType eventType) {
        Optional<EventType> optionalEventType = eventTypeRepository.findByName(eventType.getName());
        if (optionalEventType.isPresent()) {
            log.error(ErrorMessages.ERROR_EVENT_TYPE_EXISTS + " name: {}", eventType.getName());
            throw new ResourceAlreadyExistsException(ErrorMessages.ERROR_EVENT_TYPE_EXISTS);
        }
        eventTypeRepository.save(eventType);
    }

    @Override
    public Iterable<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }

    @Override
    public EventType getEventTypeById(Long id) {
        Optional<EventType> eventType = eventTypeRepository.findById(id);
        if (eventType.isEmpty()) {
            log.error(ErrorMessages.ERROR_EVENT_TYPE_NOT_FOUND + " id: {}", id);
            throw new ResourceNotFoundException(ErrorMessages.ERROR_EVENT_TYPE_NOT_FOUND);
        }
        return eventType.get();
    }

    @Override
    public void deleteEventType(Long id) {
        EventType eventType = getEventTypeById(id);
        eventTypeRepository.delete(eventType);
    }

    @Override
    public EventType updateEventType(Long id, EventType eventType) {
        EventType _eventType = getEventTypeById(id);
        _eventType.setName(eventType.getName());
        return eventTypeRepository.save(_eventType);
    }
}
