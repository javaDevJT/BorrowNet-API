package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.ItemRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private PostingDTO posting;
    @NotNull
    private Integer postingId;
    private Integer requester;
    @NotNull
    private Integer itemRequestLength;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean requestReviewed;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean requestAccepted;

    public static ItemRequestDTO fromItemRequest(ItemRequest itemRequest) {
        return ItemRequestDTO.builder().id(itemRequest.getId())
                .posting(PostingDTO.fromPosting(itemRequest.getPosting()))
                .postingId(itemRequest.getPosting().getId())
                .requester(itemRequest.getRequester().getId())
                .itemRequestLength(itemRequest.getItemRequestLength())
                .requestReviewed(itemRequest.getRequestReviewed())
                .requestAccepted(itemRequest.getRequestAccepted())
                .build();
    }
}
