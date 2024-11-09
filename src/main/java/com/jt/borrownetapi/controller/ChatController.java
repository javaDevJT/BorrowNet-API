package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.ChatDTO;
import com.jt.borrownetapi.dto.ChatSummary;
import com.jt.borrownetapi.service.ChatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Transactional
    @PostMapping("/{withId}")
    public ResponseEntity<ChatDTO> sendChat(@PathVariable("withId") Integer withId,@RequestBody String message) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(chatService.sendChat(withId, message), HttpStatus.CREATED);
        }
    }


    @Transactional
    @GetMapping("/{withId}")
    public ResponseEntity<List<ChatDTO>> getChat(@PathVariable("withId") Integer withId) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(chatService.getChat(withId),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<ChatSummary>> getChatSummary() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(chatService.getChatSummary(),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }
}
