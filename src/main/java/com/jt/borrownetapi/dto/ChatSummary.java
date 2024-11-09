package com.jt.borrownetapi.dto;

import com.jt.borrownetapi.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSummary {
    String senderUsername;
    Integer senderId;
    String targetUsername;
    Integer targetUserId;
    String messagePreview;
    Date lastMessage;

    public static ChatSummary fromChat(Chat chat) {
        return ChatSummary.builder().senderUsername(chat.getSender().getFirstName() + " " + chat.getSender().getLastName())
                .senderId(chat.getSender().getId())
                .targetUsername(chat.getTarget().getFirstName() + " " + chat.getTarget().getLastName())
                .targetUserId(chat.getTarget().getId())
                .messagePreview(chat.getMessage().length() > 36 ? chat.getMessage().substring(0, 33) + "..." : chat.getMessage())
                .lastMessage(chat.getSendTime())
                .build();
    }
}
