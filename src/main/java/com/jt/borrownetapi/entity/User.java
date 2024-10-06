package com.jt.borrownetapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrownet_user", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {
    @Id
    @Column
    private Integer id;
    @Column
    @NotNull
    private String firstName;
    @Column
    @NotNull
    private String lastName;
    @Column
    @NotNull
    private String email;
    @Column
    private Date date;
    @Column
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column
    @NotNull
    private String role = "ROLE_USER";
    @MapsId
    @OneToOne
    private UserPreferences userPreferences;
}
