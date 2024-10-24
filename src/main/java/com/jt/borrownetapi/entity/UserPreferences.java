package com.jt.borrownetapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    @NotNull
    private double borrowDistanceKM = 10.0;
    @Column
    private String profileDescription;
    @Column(columnDefinition = "VARCHAR(10485760)")
    private String profilePicture;
}
