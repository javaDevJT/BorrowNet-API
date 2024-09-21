package com.jt.borrownetapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class UserPreferences {
    @Id
    private int id;
    @NotNull
    private double borrowDistanceKM = 10.0;
    private String profileDescription;
    @NotNull
    private byte[] profilePicture;
}
