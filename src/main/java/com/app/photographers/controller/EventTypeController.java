package com.app.photographers.controller;

import com.app.photographers.model.EventType;
import com.app.photographers.service.EventTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/event-types")
@AllArgsConstructor
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @PostMapping
    public void createEventType(@Valid @RequestBody EventType eventType) {
        eventTypeService.saveEventType(eventType);
    }

    @GetMapping
    public ResponseEntity<List<EventType>> getAllEventTypes() {
        List<EventType> eventTypes = (List<EventType>) eventTypeService.getAllEventTypes();
        return ResponseEntity.ok(eventTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventType> getEventTypeById(@PathVariable Long id) {
        EventType eventType = eventTypeService.getEventTypeById(id);
        return ResponseEntity.ok(eventType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventType> updateEventType(@PathVariable Long id, @RequestBody EventType eventType) {
        return ResponseEntity.ok(eventTypeService.updateEventType(id, eventType));
    }

    @DeleteMapping("/{id}")
    public void deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
    }
}
