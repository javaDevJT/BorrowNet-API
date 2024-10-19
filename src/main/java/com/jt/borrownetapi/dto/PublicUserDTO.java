package com.jt.borrownetapi.dto;

import com.jt.borrownetapi.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDTO {
    @NotNull
    private Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    private UserPreferencesPublicDTO userPreferences;

    public static PublicUserDTO fromUser(User user) {
        return PublicUserDTO.builder().email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .id(user.getId())
                .userPreferences(UserPreferencesPublicDTO.fromUserPreferences(user.getUserPreferences()))
                .build();
    }
}
