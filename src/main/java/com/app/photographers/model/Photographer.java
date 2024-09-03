package com.app.photographers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "photographers", indexes = @Index(columnList = "modifiedDate", name = "idx_modified_date"))
@Data
public class Photographer extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name should not be more than 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description should not be more than 1000 characters")
    @Column(length = 1000, nullable = false)
    private String description;

    @NotBlank(message = "Contact is required")
    @Size(max = 20, message = "Contact should not be more than 20 characters")
    @Column(length = 20, nullable = false)
    private String contact;

    @Size(max = 255, message = "Avatar URL should not be more than 255 characters")
    @Column
    private String avatar;

    @NotNull(message = "Event type is required")
    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;
}
