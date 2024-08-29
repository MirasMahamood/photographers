package com.app.photographers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Username is required")
    @Column(unique=true, nullable = false)
    private String username;

    @NotNull(message = "Password is required")
    @Column(nullable = false)
    private String password;
}
