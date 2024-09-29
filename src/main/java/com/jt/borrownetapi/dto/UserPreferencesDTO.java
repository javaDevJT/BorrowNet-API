package com.jt.borrownetapi.dto;

import com.jt.borrownetapi.entity.UserPreferences;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesDTO {

    public static UserPreferencesDTO fromUserPreferences(UserPreferences userPreferencesDTO) {
        return UserPreferencesDTO.builder().borrowDistanceKM(userPreferencesDTO.getBorrowDistanceKM())
                .profileDescription(userPreferencesDTO.getProfileDescription())
                .profilePicture(userPreferencesDTO.getProfilePicture())
                .build();
    }

    private int id;
    @NotNull
    private double borrowDistanceKM = 10.0;
    private String profileDescription;
    @NotNull
    private byte[] profilePicture;
}
