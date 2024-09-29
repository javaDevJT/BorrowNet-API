package com.jt.borrownetapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class UserPreferences {
    @Id
    @Column
    private int id;
    @Column
    @NotNull
    private double borrowDistanceKM = 10.0;
    @Column
    private String profileDescription;
    @Column
    @NotNull
    private byte[] profilePicture;
}
