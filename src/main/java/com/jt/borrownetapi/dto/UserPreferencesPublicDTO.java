package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserPreferencesPublicDTO {

    public static UserPreferencesPublicDTO fromUserPreferences(UserPreferences userPreferencesDTO) {
        return UserPreferencesPublicDTO.builder()
                .profileDescription(userPreferencesDTO.getProfileDescription())
                .profilePicture(userPreferencesDTO.getProfilePicture())
                .build();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String profileDescription;
    @NotNull
    private String profilePicture;
}
