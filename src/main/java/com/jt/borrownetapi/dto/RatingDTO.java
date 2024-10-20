package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.Rating;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PublicUserDTO ratedUser;
    @NotNull
    @Pattern(regexp = "[1-5]")
    private Integer rating;

    private PublicUserDTO submitter;
    @NotNull
    @Length(max = 500)
    private String details;

    public static RatingDTO fromRating(Rating rating) {
        return RatingDTO.builder().id(rating.getId())
                .ratedUser(PublicUserDTO.fromUser(rating.getRatedUser()))
                .rating(rating.getRating())
                .submitter(PublicUserDTO.fromUser(rating.getSubmitter()))
                .details(rating.getDetails())
                .build();
    }
}
