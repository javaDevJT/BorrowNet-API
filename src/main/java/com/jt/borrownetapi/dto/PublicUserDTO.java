package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RatingDTO> ratingsReceived;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PostingDTO> postings;

    public static PublicUserDTO fromUser(User user) {
        return PublicUserDTO.builder().email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .id(user.getId())
                .postings(user.getPostings() != null ? user.getPostings().stream().map(PostingDTO::fromPosting).toList() : null)
                .ratingsReceived(user.getRatingsReceived() != null ? user.getRatingsReceived().stream().map(RatingDTO::fromRating).toList() : null)
                .userPreferences(UserPreferencesPublicDTO.fromUserPreferences(user.getUserPreferences()))
                .build();
    }
}
