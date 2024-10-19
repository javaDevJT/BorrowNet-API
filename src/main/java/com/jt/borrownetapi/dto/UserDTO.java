package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private Date date;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull
    private String role = "ROLE_USER";
    private UserPreferencesDTO userPreferences;

    public static UserDTO fromUser(User user) {
        return UserDTO.builder().id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .userPreferences(UserPreferencesDTO.builder()
                        .id(user.getUserPreferences().getId())
                        .borrowDistanceKM(user.getUserPreferences().getBorrowDistanceKM())
                        .profilePicture(user.getUserPreferences().getProfilePicture())
                        .profileDescription(user.getUserPreferences().getProfileDescription())
                        .build())
                .build();
    }
}
