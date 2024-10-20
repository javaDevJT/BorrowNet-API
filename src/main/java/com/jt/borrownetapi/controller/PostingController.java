package com.jt.borrownetapi.controller;

import com.jt.borrownetapi.dto.PostingDTO;
import com.jt.borrownetapi.service.PostingService;
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
@RequestMapping("/posts")
public class PostingController {
    @Autowired
    private PostingService postingService;

    @GetMapping("/{id}")
    public ResponseEntity<PostingDTO> getPostById(@PathVariable("id") Integer id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.getPostingById(id), HttpStatus.OK);
        }
    }

    @GetMapping("/list")
    @Transactional
    public ResponseEntity<List<PostingDTO>> getPostings(@RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        @RequestParam(defaultValue = "id") String sortBy) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.getPostings(pageNo, pageSize, sortBy),
                    HttpStatusCode.valueOf(HttpStatus.OK.value()));
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PostingDTO> createPosting(PostingDTO postingDTO) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new BadCredentialsException("User is not logged in.");
        } else {
            return new ResponseEntity<>(postingService.createPosting(postingDTO),
                    HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
        }
    }
}
