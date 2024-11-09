package com.jt.borrownetapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jt.borrownetapi.entity.Chat;
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
public class ChatDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private PublicUserDTO sender;
    private PublicUserDTO target;
    @NotNull
    private Integer senderId;
    @NotNull
    private Integer targetId;
    @NotNull
    private String message;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date sendTime;

    public static ChatDTO fromChat(Chat chat) {
        return ChatDTO.builder().id(chat.getId())
                .sender(PublicUserDTO.fromUser(chat.getSender()))
                .target(PublicUserDTO.fromUser(chat.getTarget()))
                .senderId(chat.getSender().getId())
                .targetId(chat.getTarget().getId())
                .message(chat.getMessage())
                .sendTime(chat.getSendTime())
                .build();
    }
}
