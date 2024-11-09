package com.jt.borrownetapi.service;

import com.jt.borrownetapi.dto.ChatDTO;
import com.jt.borrownetapi.dto.ChatSummary;
import com.jt.borrownetapi.entity.Chat;
import com.jt.borrownetapi.entity.User;
import com.jt.borrownetapi.repository.ChatRepository;
import com.jt.borrownetapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatService {

    UserRepository userRepository;

    ChatRepository chatRepository;

    public ChatDTO sendChat(Integer withId, String message) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Optional<User> byId = userRepository.findById(withId);
        if (byId.isEmpty() || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Target user does not exist or message is blank.");
        }
        Chat chat = Chat.builder()
                .message(message)
                .sender(userByEmail)
                .target(byId.get())
                .sendTime(new Date())
                .build();
        chatRepository.save(chat);
        return ChatDTO.fromChat(chat);
    }

    public List<ChatDTO> getChat(Integer withId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        Optional<User> byId = userRepository.findById(withId);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Target user does not exist or message is blank.");
        }
        User withUser = byId.get();
        List<Chat> byTargetInAndSenderInOrderBySendTimeDesc =
                chatRepository.findByTargetInAndSenderInOrderBySendTimeDesc(List.of(withUser, userByEmail), List.of(withUser, userByEmail));
        return byTargetInAndSenderInOrderBySendTimeDesc.stream().map(ChatDTO::fromChat).toList();
    }

    public List<ChatSummary> getChatSummary() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (userByEmail == null) {
            throw new RuntimeException("User object does not exist for security context");
        }
        List<Chat> recentChats = chatRepository.findRecentChatSessions(userByEmail);
        return recentChats.stream().map(ChatSummary::fromChat).toList();
    }
}
