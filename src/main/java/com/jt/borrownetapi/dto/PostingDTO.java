package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.Posting;
import com.jt.borrownetapi.model.enums.Condition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotNull
    private PublicUserDTO lender;
    @NotNull
    private String itemName;
    @Length(max = 500, message = "Item description max length is 500 characters.")
    private String itemDescription;
    @NotNull
    private String itemPhoto;
    @NotNull
    private Integer maxRentalDays;
    private Condition condition;


    public static PostingDTO fromPosting(Posting posting) {
        return PostingDTO.builder().id(posting.getId())
                .lender(PublicUserDTO.fromUser(posting.getLender()))
                .itemName(posting.getItemName())
                .itemDescription(posting.getItemDescription())
                .itemPhoto(posting.getItemPhoto())
                .maxRentalDays(posting.getMaxRentalDays())
                .condition(posting.getCondition())
                .build();
    }
}
