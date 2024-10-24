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
    private Integer ratedUser;
    @NotNull
    @Pattern(regexp = "[1-5]")
    private Integer rating;

    private Integer submitter;
    @NotNull
    @Length(max = 500)
    private String details;

    public static RatingDTO fromRating(Rating rating) {
        return RatingDTO.builder().id(rating.getId())
                .ratedUser(rating.getRatedUser().getId())
                .rating(rating.getRating())
                .submitter(rating.getSubmitter().getId())
                .details(rating.getDetails())
                .build();
    }
}
