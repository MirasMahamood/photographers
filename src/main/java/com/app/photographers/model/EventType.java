package com.app.photographers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "event_types")
@Data
public class EventType {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Event name is required")
    @Column(unique = true, nullable = false)
    private String name;
}
